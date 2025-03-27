package com.rip.shrimptank.firebase

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.rip.shrimptank.model.User
import com.rip.shrimptank.utils.UserInteractions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rip.shrimptank.ShrimpTank
import com.rip.shrimptank.model.Cloudinary
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(private val auth: FirebaseAuth, private val firebaseFireStore: FirebaseFirestore) :
    FirebaseRepository {
    private val usersRef: CollectionReference = firebaseFireStore.collection("USERS")
    override fun register(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    UserInteractions.userId = getid()
                    callback(true, null)
                } else {
                    val errorMessage = getFirebaseErrorMessage(task.exception)
                    callback(false, errorMessage)
                }
            }
    }

    override fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    val errorMessage = when (val exception = task.exception) {
                        is FirebaseAuthException -> {
                            when (exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "The email address is badly formatted."
                                "ERROR_USER_NOT_FOUND" -> "No account found for this email. Please sign up first."
                                "ERROR_WRONG_PASSWORD" -> "The password is incorrect. Please try again."
                                "ERROR_USER_DISABLED" -> "This account has been disabled."
                                else -> "Authentication failed. Please try again."
                            }
                        }
                        else -> exception?.localizedMessage ?: "Unknown error occurred. Please try again."
                    }
                    callback(false, errorMessage)
                }
            }
    }

    override fun saveUser(user: User, imageUri: Uri) {
        val id = auth.currentUser?.uid
        user.id = id!!
        uploadImageToCloudinary(imageUri) { success, downloadUrl ->
            if (success) {
                user.avatar = downloadUrl
                usersRef.document(id).set(user)
            } else {
                println("Failed to upload image: $downloadUrl")
            }
        }
    }


    override fun updateUser(user: User, imageUri: Uri?, callback: (Boolean, String?) -> Unit) {
        val id = auth.currentUser?.uid
        user.id = id!!
        if (imageUri == null) {
            val updates = hashMapOf<String, Any>(
                "name" to user.name,
            )
            usersRef.document(id).update(updates)
            callback(true, "User Detail updated Successfully")
            return
        }

        saveUser(user,imageUri)

    }

    override fun checkAuth(): Boolean {
        if(auth.currentUser != null){
            UserInteractions.userId = auth.currentUser!!.uid
        }
        return auth.currentUser != null
    }

    override fun getid(): String {
        return auth.currentUser!!.uid
    }

    override fun fetchUser(id: String) {
        usersRef.document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        UserInteractions.userData = user
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error fetching user data", exception)
            }
    }

    override fun checkIfUserLoggedIn(callback: (User?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            callback(null)
            return
        }

        usersRef.document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val user = doc.toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    override fun logout() {
        auth.signOut()
    }

    private fun uploadImageToCloudinary(imageUri: Uri, callback: (Boolean, String?) -> Unit) {
        try {
            val context = ShrimpTank.Globals.context ?: throw IllegalStateException("Context is null")
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

            Cloudinary.shared.uploadBitmap(bitmap, { imageUrl ->
                callback(true, imageUrl)
            }, { errorMessage ->
                Log.e("Cloudinary", "Upload failed: $errorMessage")
                callback(false, errorMessage)
            })
        } catch (e: Exception) {
            Log.e("Cloudinary", "Error uploading image: ${e.message}", e)
            callback(false, e.message)
        }
    }

    private fun getFirebaseErrorMessage(exception: Exception?): String {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> "The password is too weak. Please choose a stronger password."
            is FirebaseAuthInvalidCredentialsException -> "The email address is badly formatted. Please enter a valid email."
            is FirebaseAuthUserCollisionException -> "An account already exists with this email. Please log in or use a different email."
            is FirebaseAuthInvalidUserException -> "The user does not exist or has been disabled."
            is FirebaseAuthException -> "Authentication failed: ${exception.message}"
            else -> "An unknown error occurred. Please try again later."
        }
    }
}
package com.rip.shrimptank.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.rip.shrimptank.firebase.FirebaseRepository
import com.rip.shrimptank.model.User
import com.rip.shrimptank.utils.ValidationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel(){

    fun validateAndLogin(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        val isEmailValid = ValidationUtil.isEmailValid(email)
        val isPasswordValid = ValidationUtil.isPasswordValid(password)

        if (isEmailValid && isPasswordValid) {
            login(email, password, callback)
        } else {
            when {
                email.isBlank() -> callback(false,"Email field cannot be empty")
                password.isBlank() -> callback(false,"Password field cannot be empty")
                !isEmailValid -> callback(false,"Invalid email address")
                !isPasswordValid -> callback(false,"Password must be at least 6 characters")
                else -> callback(false,"")
            }
        }
    }

    fun validateAndRegister(
        imageUri:Uri?,
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val isImageUri = imageUri == null
        val isNameEmpty = name.isBlank()
        val isEmailEmpty = email.isBlank()
        val isEmailValid = ValidationUtil.isEmailValid(email)
        val isPasswordEmpty = password.isBlank()
        val isPasswordValid = ValidationUtil.isPasswordValid(password)
        val isConfirmPasswordEmpty = confirmPassword.isBlank()
        val isBothPasswordsSame = password.lowercase() == confirmPassword.lowercase()

        if (
            !isImageUri &&
            !isNameEmpty &&
            !isEmailEmpty &&
            isEmailValid &&
            !isPasswordEmpty &&
            isPasswordValid &&
            !isConfirmPasswordEmpty &&
            isBothPasswordsSame
        ) {
            register(email, password, callback)
        } else {
            when {
                isImageUri -> callback(false,"Profile Image is required")
                isNameEmpty -> callback(false,"Name Field cannot be empty")
                isEmailEmpty -> callback(false,"Email field cannot be empty")
                !isEmailValid -> callback(false,"Invalid email address")
                isPasswordEmpty -> callback(false,"Password field cannot be empty")
                isConfirmPasswordEmpty -> callback(false,"Confirm Password field cannot be empty")
                !isPasswordValid -> callback(false,"Password must be at least 6 characters")
                !isBothPasswordsSame -> callback(false,"Password and confirm password are not same")
                else -> callback(false,"")
            }
        }
    }

    private fun register(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        firebaseRepository.register(email, password, callback)
    }

    private fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        firebaseRepository.login(email, password, callback)
    }

    fun fetchUserDetails(id:String){
        firebaseRepository.fetchUser(id)
    }

    fun checkIfUserLoggedIn(callback: (User?) -> Unit) {
        firebaseRepository.checkIfUserLoggedIn(callback)
    }

    fun saveUser(user: User, imageUri: Uri?) {
        firebaseRepository.saveUser(user,imageUri!!)
    }

    fun updateUser(user: User,imageUri: Uri?,callback: (Boolean, String?) -> Unit) {
        firebaseRepository.updateUser(user,imageUri,callback)
    }

    fun checkAuth(): Boolean {
        return firebaseRepository.checkAuth()
    }

    fun getid(): String {
        return firebaseRepository.getid()
    }

    fun logout(){
        firebaseRepository.logout()
    }


}
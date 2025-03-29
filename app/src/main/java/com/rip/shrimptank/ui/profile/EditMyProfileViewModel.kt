package com.rip.shrimptank.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.rip.shrimptank.model.user.User
import com.rip.shrimptank.model.user.UserModel

class EditMyProfileViewModel : ViewModel() {
    val userId = Firebase.auth.currentUser!!.uid
    var imageChanged = false
    var selectedImageURI: MutableLiveData<Uri> = MutableLiveData()
    var user: LiveData<User> = UserModel.instance.getCurrentUser()

    var name: String? = null
    var nameError = MutableLiveData("")

    private val _isUpdating = MutableLiveData<Boolean>()
    val isUpdating: LiveData<Boolean> = _isUpdating

    fun loadUser() {
        user.observeForever {
            if (it != null) {
                selectedImageURI.postValue(Uri.parse(it.avatar))
            }
        }
    }

    override fun onCleared() {
        user.removeObserver {}
        super.onCleared()
    }

    fun updateUser(updatedUserCallback: () -> Unit) {
        if (validateUserUpdate()) {
            _isUpdating.value = true
            val updatedUser = User(
                userId,
                name!!,
                selectedImageURI.value!!.toString()
            )

            UserModel.instance.updateUser(updatedUser) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(selectedImageURI.value!!)
                    .setDisplayName("$name")
                    .build()

                Firebase.auth.currentUser!!.updateProfile(profileUpdates).addOnSuccessListener {
                    if (imageChanged) {
                        UserModel.instance.updateUserImage(user.value!!, selectedImageURI.value!!) {
                            _isUpdating.value = false
                            updatedUserCallback()
                        }
                    } else {
                        _isUpdating.value = false
                        updatedUserCallback()
                    }
                }.addOnFailureListener {
                    _isUpdating.value = false
                }
            }
        }
    }

    private fun validateUserUpdate(): Boolean {
        if (name!!.isEmpty()) {
            nameError.postValue("Name cannot be empty")
            return false
        }
        return true
    }
}
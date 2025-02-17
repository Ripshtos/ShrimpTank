package com.rip.shrimptank.firebase

import android.net.Uri
import com.rip.shrimptank.model.User

interface FirebaseRepository {

    fun register(email: String, password: String,callback: (Boolean, String?) -> Unit)
    fun login(email: String, password: String,callback: (Boolean, String?) -> Unit)
    fun saveUser(user: User, imageUri: Uri)
    fun updateUser(user: User,imageUri:Uri?,callback: (Boolean, String?) -> Unit)
    fun checkAuth():Boolean
    fun getid():String
    fun fetchUser(id:String)
    fun logout()

}
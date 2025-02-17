package com.rip.shrimptank.repository

import com.rip.shrimptank.firebase.FirebaseRepository
import com.rip.shrimptank.room.AppDao
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val auth: FirebaseAuth,
                                             private val firebaseRepository: FirebaseRepository, private val appDao: AppDao) {


}
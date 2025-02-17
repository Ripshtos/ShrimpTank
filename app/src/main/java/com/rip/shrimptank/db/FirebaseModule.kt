package com.rip.shrimptank.db

import com.rip.shrimptank.firebase.FirebaseRepository
import com.rip.shrimptank.firebase.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStoreRepository(auth: FirebaseAuth, firebaseFireStore: FirebaseFirestore): FirebaseRepository {
        return FirebaseRepositoryImpl(auth,firebaseFireStore)
    }

}
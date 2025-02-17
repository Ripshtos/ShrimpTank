package com.rip.shrimptank.db

import android.content.Context
import androidx.room.Room
import com.rip.shrimptank.firebase.FirebaseRepository
import com.rip.shrimptank.repository.DatabaseRepository
import com.rip.shrimptank.room.AppDao
import com.rip.shrimptank.room.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDao(appDatabase: AppDatabase): AppDao = appDatabase.appDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "local_event_hub_database"
        )
            .build()

    @Provides
    fun provideDatabaseRepository(auth: FirebaseAuth,
                                  firebaseRepository: FirebaseRepository, appDao: AppDao): DatabaseRepository = DatabaseRepository(auth,firebaseRepository,appDao)
}
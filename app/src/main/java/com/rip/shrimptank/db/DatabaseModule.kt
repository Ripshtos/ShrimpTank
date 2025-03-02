package com.rip.shrimptank.db

import com.rip.shrimptank.room.CartDao
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
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_store_database"
        ).build()
    }

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao {
        return db.cartDao()
    }

    @Provides
    fun provideDatabaseRepository(auth: FirebaseAuth,
                                  firebaseRepository: FirebaseRepository, appDao: AppDao): DatabaseRepository = DatabaseRepository(auth,firebaseRepository,appDao)
}
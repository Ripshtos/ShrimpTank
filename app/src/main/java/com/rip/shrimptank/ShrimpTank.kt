package com.rip.shrimptank

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import android.content.Context
import android.util.Log

@HiltAndroidApp
class ShrimpTank : Application() {
    object Globals {
        var context: Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        Globals.context = applicationContext
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("UncaughtException", "Uncaught exception in thread ${thread.name}", throwable)
        }
    }
}
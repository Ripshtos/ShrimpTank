package com.rip.shrimptank

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShrimpTank : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
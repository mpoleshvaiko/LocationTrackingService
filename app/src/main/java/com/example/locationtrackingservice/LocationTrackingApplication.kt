package com.example.locationtrackingservice

import android.app.Application
import com.example.locationtrackingservice.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class LocationTrackingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@LocationTrackingApplication)
            workManagerFactory()
            modules(appModule)
        }
    }
}
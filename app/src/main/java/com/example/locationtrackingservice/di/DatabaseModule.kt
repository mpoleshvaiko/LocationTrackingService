package com.example.locationtrackingservice.di

import androidx.room.Room
import com.example.locationtrackingservice.database.LocationDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val database = module {
    single {
        Room
            .databaseBuilder(
                androidContext(),
                LocationDatabase::class.java,
                "location-database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<LocationDatabase>().locationDao() }
}
package com.example.locationtrackingservice.di

import com.example.locationtrackingservice.worker.SaveLocationWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workManager = module {
    worker { SaveLocationWorker(get(), get(), get(), get()) }
}
package com.example.locationtrackingservice.di

import com.example.locationtrackingservice.repository.LocationRepository
import com.example.locationtrackingservice.repository.LocationRepositoryImpl
import org.koin.dsl.module

val repository = module {
    single<LocationRepository> { LocationRepositoryImpl(get()) }
}
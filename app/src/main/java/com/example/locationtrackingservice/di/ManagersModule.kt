package com.example.locationtrackingservice.di

import com.example.locationtrackingservice.managers.location.LocationManager
import com.example.locationtrackingservice.managers.location.LocationManagerImpl
import com.example.locationtrackingservice.managers.map.MapManager
import com.example.locationtrackingservice.managers.map.MapManagerImpl
import com.example.locationtrackingservice.managers.permission.PermissionManager
import com.example.locationtrackingservice.managers.permission.PermissionManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managers = module {
    single<LocationManager> { LocationManagerImpl(androidContext()) }
    single<MapManager> { MapManagerImpl() }
    single<PermissionManager> { PermissionManagerImpl(androidContext()) }
}
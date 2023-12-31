package com.example.locationtrackingservice.di

import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachineImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val stateMachine = module {
    single<LocationTrackingStateMachine> {
        LocationTrackingStateMachineImpl(
            get(),
            androidContext()
        )
    }
}
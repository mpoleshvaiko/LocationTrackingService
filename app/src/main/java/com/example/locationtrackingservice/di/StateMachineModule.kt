package com.example.locationtrackingservice.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachineImpl
import com.google.android.gms.maps.MapView
import org.koin.dsl.module

val stateMachine = module {
    single<LocationTrackingStateMachine> { LocationTrackingStateMachineImpl(get(), get(), get()) }
    single { MutableLiveData<MapView>() }
    single { MutableLiveData<Unit>() as LiveData<Unit> }
}
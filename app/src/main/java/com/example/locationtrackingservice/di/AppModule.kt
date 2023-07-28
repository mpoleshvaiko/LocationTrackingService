package com.example.locationtrackingservice.di

import org.koin.dsl.module

val appModule = module {
    includes(
        viewModelModule,
        managers,
        stateMachine,
        database,
        repository
    )
}
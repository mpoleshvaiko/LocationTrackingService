package com.example.locationtrackingservice.di

import com.example.locationtrackingservice.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainActivityViewModel(get(), get(), get(), get()) }
}
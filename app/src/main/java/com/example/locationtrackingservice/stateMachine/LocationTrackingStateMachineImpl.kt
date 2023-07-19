package com.example.locationtrackingservice.stateMachine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.locationtrackingservice.LOG_TAG_STATE


class LocationTrackingStateMachineImpl : LocationTrackingStateMachine {
    private val _currentState = MutableLiveData<States>(States.IDLE)
    val currentState: LiveData<States> get() = _currentState

    override fun transitionTo(newState: States) {
        handleStateTransition(newState)
    }

    private fun handleStateTransition(newState: States) {
        when (newState) {
            States.IDLE -> {
                Log.d(LOG_TAG_STATE, "IDLE")
            }
            States.READY -> {
                Log.d(LOG_TAG_STATE, "READY")
            }
            States.RUNNING -> {
                Log.d(LOG_TAG_STATE, "RUNNING")
            }
            States.PAUSE -> {
                Log.d(LOG_TAG_STATE, "PAUSE")
            }
            States.DONE -> {
                Log.d(LOG_TAG_STATE, "DONE")
            }
            States.ERROR -> {
                Log.e(LOG_TAG_STATE, "ERROR")
            }
        }
        _currentState.value = newState
    }
}
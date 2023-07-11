package com.example.locationtrackingservice.stateMachine

interface LocationTrackingStateMachine {
    fun transitionTo(newState: States)
}
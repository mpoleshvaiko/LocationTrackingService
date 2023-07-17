package com.example.locationtrackingservice.stateMachine

import com.google.android.gms.maps.MapView

interface LocationTrackingStateMachine {
    fun transitionTo(newState: States)
    fun setMapView(mapView: MapView)
}
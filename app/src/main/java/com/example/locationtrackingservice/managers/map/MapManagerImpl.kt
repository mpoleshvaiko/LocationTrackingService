package com.example.locationtrackingservice.managers.map

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapManagerImpl : MapManager {
    private var currentMarker: Marker? = null
    private var hasZoomed = false
    override fun displayLocation(location: Location, mapView: MapView?) {
        mapView?.let { map ->
            map.getMapAsync { maps ->
                val latLng = LatLng(location.latitude, location.longitude)

                if (!hasZoomed) {
                    maps.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    hasZoomed = true
                }

                currentMarker?.remove()

                val markerOptions = MarkerOptions().position(latLng).title("Current Location")
                currentMarker = maps.addMarker(markerOptions)
            }
        }
    }
}
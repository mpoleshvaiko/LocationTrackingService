package com.example.locationtrackingservice.managers.map

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapManagerImpl : MapManager {
    override fun displayLocation(location: Location, mapView: MapView?) {
        mapView?.let { map ->
            map.getMapAsync { maps ->
                val latLng = LatLng(location.latitude, location.longitude)
                val markerOptions =
                    MarkerOptions().position(latLng).title("Current Location")

                maps.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                maps.addMarker(markerOptions)
            }
        }
    }
}
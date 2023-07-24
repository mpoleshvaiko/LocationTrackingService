package com.example.locationtrackingservice.managers.map

import android.content.Context
import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapManagerImpl : MapManager {
    private var currentMarker: Marker? = null
    private var hasZoomed = false
    private val zoom = 15f

    override fun displayLocation(location: Location, mapView: MapView?) {
        mapView?.let { map ->
            map.getMapAsync { maps ->
                val latLng = LatLng(location.latitude, location.longitude)

                if (!hasZoomed) {
                    maps.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
                    hasZoomed = true
                }

                currentMarker?.remove()

                val markerOptions = MarkerOptions().position(latLng).title("Current Location")
                currentMarker = maps.addMarker(markerOptions)
            }
        }
    }

    override fun onSaveMapState(map: GoogleMap, mapView: MapView) {
        val sharedPreferences =
            mapView.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()) {
            putFloat(KEY_LAST_ZOOM_LEVEL, map.cameraPosition.zoom)
            putBoolean(KEY_HAS_ZOOMED, hasZoomed)
            apply()
        }
    }

    override fun onRestoreMapState(map: GoogleMap, mapView: MapView) {
        val sharedPreferences =
            mapView.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastZoomLevel = sharedPreferences.getFloat(KEY_LAST_ZOOM_LEVEL, zoom)
        hasZoomed = sharedPreferences.getBoolean(KEY_HAS_ZOOMED, false)

        if (hasZoomed) {
            val cameraPosition = CameraPosition.Builder()
                .target(map.cameraPosition.target)
                .zoom(lastZoomLevel)
                .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
}

const val PREFS_NAME = "MapPrefs"
const val KEY_LAST_ZOOM_LEVEL = "lastZoomLevel"
const val KEY_HAS_ZOOMED = "hasZoomed"
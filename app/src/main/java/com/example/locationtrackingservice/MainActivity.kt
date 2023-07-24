package com.example.locationtrackingservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.example.locationtrackingservice.databinding.ActivityMainBinding
import com.example.locationtrackingservice.managers.map.MapManager
import com.google.android.gms.maps.MapView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()
    private lateinit var mapView: MapView
    private val mapManager: MapManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = viewModel
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        viewModel.requestPermissions(this) { granted ->
            if (granted) {
                getCurrentLocationAndDisplayOnMap()
                viewModel.readyToTrack()
            } else Log.e(LOG_TAG_PERMISSIONS, "PERMISSIONS WERE NOT GRANTED")
        }
        mapView.getMapAsync { mapManager.onRestoreMapState(it, mapView) }
    }

    private fun getCurrentLocationAndDisplayOnMap() =
        viewModel.startLocationUpdates().observe(this) { location ->
            if (location != null) {
                mapManager.displayLocation(location, mapView)
            } else {
                Log.e(LOG_TAG_LOCATION, "FAILED TO OBTAIN CURRENT LOCATION")
            }
        }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        viewModel.stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
        mapView.getMapAsync {
            mapManager.onSaveMapState(it, mapView)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.handlePermissionResult(requestCode, permissions, grantResults)
    }
}
package com.example.locationtrackingservice

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.locationtrackingservice.database.LocationEntity
import com.example.locationtrackingservice.databinding.ActivityMainBinding
import com.example.locationtrackingservice.managers.map.MapManager
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()
    private lateinit var mapView: MapView
    private val mapManager: MapManager by inject()
    private var polyline: Polyline? = null

    private val requestAppSettings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (viewModel.isPermissionGranted()) {
                    getCurrentLocationAndDisplayOnMap()
                    viewModel.readyToTrack()
                } else {
                    showPermissionSnackbar()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = viewModel
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        requestPermissions()
        mapView.getMapAsync { mapManager.onRestoreMapState(it, mapView) }
        getLocationsAndDrawPath()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        if (viewModel.isPermissionGranted()) {
            getCurrentLocationAndDisplayOnMap()
            viewModel.readyToTrack()
        } else Log.e(LOG_TAG_PERMISSIONS, "PERMISSIONS WERE NOT GRANTED")

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

    private fun requestPermissions() {
        viewModel.requestPermissions(this) { granted ->
            if (granted) {
                getCurrentLocationAndDisplayOnMap()
                viewModel.readyToTrack()
            } else {
                showPermissionSnackbar()
            }
        }
    }

    private fun getCurrentLocationAndDisplayOnMap() =
        viewModel.startLocationUpdates().onEach { location ->
            if (location != null) {
                mapManager.displayLocation(location, mapView)
            } else {
                Log.e(LOG_TAG_LOCATION, "FAILED TO OBTAIN CURRENT LOCATION")
            }
        }.launchIn(lifecycleScope)

    private fun showPermissionSnackbar() {
        binding.root.showSnackbar(
            getString(R.string.location_permission_required_text),
            Snackbar.LENGTH_LONG,
            getString(R.string.retry_button_text)
        ) {
            navigateToAppSettings()
        }
    }

    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        requestAppSettings.launch(intent)
    }

    private fun getLocationsAndDrawPath() =
        viewModel.collectedLocations.observe(this) { locations ->
            drawPathOnMap(locations)
        }

    private fun drawPathOnMap(locations: List<LocationEntity>) {
        mapView.getMapAsync { map ->

            polyline?.remove()

            val pathOptions = PolylineOptions().apply {
                width(10f)
                color(Color.BLUE)
                geodesic(true)
            }

            for (location in locations) {
                val latLng = LatLng(location.latitude, location.longitude)
                pathOptions.add(latLng)
            }

            polyline = map.addPolyline(pathOptions)
        }
    }
}
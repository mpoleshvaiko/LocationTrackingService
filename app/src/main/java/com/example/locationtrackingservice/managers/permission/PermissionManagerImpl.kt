package com.example.locationtrackingservice.managers.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionManagerImpl(
    private val context: Context
) : PermissionManager {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var permissionCallback: ((Boolean) -> Unit)? = null

    override fun permissionGranted() = ContextCompat.checkSelfPermission(
        context.applicationContext,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        context.applicationContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    override fun requestPermissions(activity: AppCompatActivity, callback: (Boolean) -> Unit) {
        permissionCallback = callback
        if (permissionGranted()) {
            permissionCallback!!.invoke(true)
        } else {
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            val granted = grantResults.all {
                it == PackageManager.PERMISSION_GRANTED
            }
            permissionCallback?.invoke(granted)
        }
    }
}
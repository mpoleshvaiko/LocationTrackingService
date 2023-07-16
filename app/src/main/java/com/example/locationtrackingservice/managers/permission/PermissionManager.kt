package com.example.locationtrackingservice.managers.permission

import androidx.appcompat.app.AppCompatActivity

interface PermissionManager {
    fun permissionGranted(): Boolean
    fun requestPermissions(activity: AppCompatActivity, callback: (Boolean) -> Unit)
    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}
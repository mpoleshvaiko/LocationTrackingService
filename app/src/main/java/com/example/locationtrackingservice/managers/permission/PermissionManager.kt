package com.example.locationtrackingservice.managers.permission

interface PermissionManager {
    fun permissionGranted(): Boolean
    fun requestPermissions(callback: (Boolean) -> Unit)
    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}
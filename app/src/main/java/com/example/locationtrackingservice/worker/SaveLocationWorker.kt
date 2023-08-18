package com.example.locationtrackingservice.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.locationtrackingservice.KEY_LATITUDE
import com.example.locationtrackingservice.KEY_LONGITUDE
import com.example.locationtrackingservice.LOG_TAG_WORKER
import com.example.locationtrackingservice.database.LocationEntity
import com.example.locationtrackingservice.repository.LocationRepository

class SaveLocationWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val locationRepository: LocationRepository
) :
    CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val latitude = inputData.getDouble(KEY_LATITUDE, 0.0)
        val longitude = inputData.getDouble(KEY_LONGITUDE, 0.0)
        return try {
            val locationEntity = LocationEntity(latitude = latitude, longitude = longitude)
            locationRepository.insertLocation(locationEntity)
            Result.success()
        } catch (throwable: Throwable) {
            throwable.message?.let { Log.e(LOG_TAG_WORKER, it) }
            Result.failure()
        }
    }
}
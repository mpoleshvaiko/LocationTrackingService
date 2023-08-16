package com.example.locationtrackingservice.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.locationtrackingservice.KEY_LOCATION_DATA
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
        val location = inputData.getString(KEY_LOCATION_DATA) ?: return Result.failure()
        return try {
            val locationEntity = LocationEntity(location = location)
            locationRepository.insertLocation(locationEntity)
            Result.success()
        } catch (throwable: Throwable) {
            Log.e(LOG_TAG_WORKER, "ERROR SAVING LOCATION")
            Result.failure()
        }
    }
}
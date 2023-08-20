package com.example.locationtrackingservice.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.locationtrackingservice.LOG_TAG_WORKER
import com.example.locationtrackingservice.database.LocationEntity
import com.example.locationtrackingservice.repository.LocationRepository
import com.example.locationtrackingservice.stateMachine.LocationTrackingStateMachine

class SaveLocationWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val locationRepository: LocationRepository,
    private val stateMachine: LocationTrackingStateMachine
) :
    CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            stateMachine.getLocationUpdates().collect { location ->
                if (location != null) {
                    val locationEntity =
                        LocationEntity(latitude = location.latitude, longitude = location.longitude)
                    locationRepository.insertLocation(locationEntity)
                }
            }
            Result.success()
        } catch (throwable: Throwable) {
            throwable.message?.let { Log.e(LOG_TAG_WORKER, it) }
            Result.failure()
        }
    }
}
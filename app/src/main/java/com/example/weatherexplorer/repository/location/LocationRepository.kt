package com.example.weatherexplorer.repository.location

import io.reactivex.rxjava3.core.Single

interface LocationRepository {
    /**
     * Calls location request if location permission is granted
     * or makes location permission request
     *
     * @return HashMap with location data inside
     */
    fun getCurrentLocation(): Single<HashMap<String, Double>>
}
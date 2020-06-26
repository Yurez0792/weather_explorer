package com.example.weatherexplorer.repository.weather

import com.example.weatherexplorer.model.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

interface WeatherRepository {
    /**
     * Calls weather request according to user location
     * @param latitude - latitude coordinate of user location
     * @param longitude - longitude coordinate of user location
     *
     * @return WeatherResponse
     */
    fun getWeatherData(latitude: Double?, longitude: Double?): Single<Response<WeatherResponse>>

}
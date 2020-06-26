package com.example.weatherexplorer.network

import com.example.weatherexplorer.model.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    /**
     * Calls weather request according to user location
     * @param latitude - latitude coordinate of user location
     * @param longitude - longitude coordinate of user location
     * @param count - quantity of days for which we request weather data
     * @param units - define temperature value: units=imperial for Fahrenheit, units=metric for Celsius
     * @param appid - API ID for access to weather data
     *
     * @return WeatherResponse
     */
    @GET("data/2.5/forecast/daily")
    fun getWeatherData(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("cnt") count: String,
        @Query("units") units: String,
        @Query("appid") appid: String
    ): Single<Response<WeatherResponse>>
}
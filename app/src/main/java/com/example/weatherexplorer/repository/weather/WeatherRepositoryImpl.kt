package com.example.weatherexplorer.repository.weather

import com.example.weatherexplorer.network.RetrofitService
import com.example.weatherexplorer.network.WeatherApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import com.example.weatherexplorer.model.WeatherResponse as WeatherResponse1

class WeatherRepositoryImpl(private val retrofitService: RetrofitService) : WeatherRepository {

    companion object {
        private const val METRIC = "Metric"
        private const val OPEN_WEATHER_API_ID = "db1031cc2e8921eaa0987760c91dde71"
        private const val COUNT = "7"
    }

    override fun getWeatherData(
        latitude: Double?,
        longitude: Double?
    ): Single<Response<WeatherResponse1>> {
        return retrofitService.getWeatherRetrofit().create(WeatherApi::class.java)
            .getWeatherData(
                latitude.toString(),
                longitude.toString(),
                COUNT,
                METRIC,
                OPEN_WEATHER_API_ID
            )
    }
}
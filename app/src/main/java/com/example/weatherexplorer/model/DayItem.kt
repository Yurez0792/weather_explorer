package com.example.weatherexplorer.model

import com.google.gson.annotations.SerializedName

data class DayItem(
    @SerializedName("dt") val dt: Long,
    @SerializedName("sunrise") val sunrise: Int,
    @SerializedName("sunset") val sunset: Int,
    @SerializedName("temp") val temp: Temp?,
    @SerializedName("feels_like") val feels_like: FeelsLike,
    @SerializedName("pressure") val pressure: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("weather") val weather: List<Weather>?,
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int,
    @SerializedName("clouds") val clouds: Int
)
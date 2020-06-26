package com.example.weatherexplorer.di

import com.example.weatherexplorer.network.RetrofitService
import com.example.weatherexplorer.repository.location.LocationRepository
import com.example.weatherexplorer.repository.location.LocationRepositoryImpl
import com.example.weatherexplorer.repository.weather.WeatherRepository
import com.example.weatherexplorer.repository.weather.WeatherRepositoryImpl
import com.example.weatherexplorer.view.home.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    single { LocationRepositoryImpl(get()) as LocationRepository }
    single { RetrofitService() }
    single { WeatherRepositoryImpl(get()) as WeatherRepository }
}

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), androidContext().resources) }
}
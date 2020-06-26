package com.example.weatherexplorer

import android.app.Application
import com.example.weatherexplorer.di.module
import com.example.weatherexplorer.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherExplorerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WeatherExplorerApplication)
            modules(listOf(module, viewModelModule))
        }
    }
}
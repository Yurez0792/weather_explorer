package com.example.weatherexplorer.view.home

import android.content.res.Resources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.example.weatherexplorer.R
import com.example.weatherexplorer.model.WeatherResponse
import com.example.weatherexplorer.repository.location.LocationRepository
import com.example.weatherexplorer.repository.weather.WeatherRepository
import com.example.weatherexplorer.utils.Constants.Companion.LATITUDE
import com.example.weatherexplorer.utils.Constants.Companion.LONGITUDE
import com.example.weatherexplorer.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val resources: Resources
) : ViewModel(), LifecycleObserver {

    val mProgressLiveData: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }
    val mShowUpdateButton: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }
    val mErrorMessageLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mWeatherLiveData: SingleLiveEvent<WeatherResponse> by lazy {
        SingleLiveEvent<WeatherResponse>()
    }
    private val mCompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposeDisposables()
    }

    fun getCurrentLocation() {
        mProgressLiveData.postValue(true)
        val currentLocationDisposable = locationRepository.getCurrentLocation()
            .timeout(7, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Timber.e(it)
                onError()
            }
            .subscribe(
                {
                    Timber.d("Location successfully retrieved")
                    getWeatherData(it[LATITUDE]!!, it[LONGITUDE]!!)
                },
                {
                    Timber.e(it)
                    onError()
                }
            )
        mCompositeDisposable.add(currentLocationDisposable)
    }

    private fun getWeatherData(latitude: Double, longitude: Double) {
        val weatherDisposable = weatherRepository.getWeatherData(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Timber.e(it)
                mProgressLiveData.postValue(false)
                mShowUpdateButton.postValue(true)
                mErrorMessageLiveData.postValue(resources.getString(R.string.something_went_wrong_text))
            }
            .subscribe(
                {
                    Timber.d("Weather successfully retrieved")
                    if (it != null) {
                        mWeatherLiveData.postValue(it.body())
                    }
                    mProgressLiveData.postValue(false)
                },
                {
                    Timber.e(it)
                    onError()
                }
            )
        mCompositeDisposable.add(weatherDisposable)
    }

    private fun onError() {
        mProgressLiveData.postValue(false)
        mShowUpdateButton.postValue(true)
        mErrorMessageLiveData.postValue(resources.getString(R.string.something_went_wrong_text))
    }

    private fun disposeDisposables() {
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
    }
}
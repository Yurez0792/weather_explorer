package com.example.weatherexplorer.repository.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.HandlerThread
import com.example.weatherexplorer.utils.Constants.Companion.LATITUDE
import com.example.weatherexplorer.utils.Constants.Companion.LONGITUDE
import io.reactivex.rxjava3.core.Single
import timber.log.Timber

class LocationRepositoryImpl(private val context: Context) : LocationRepository {

    companion object {
        const val LOCATION_HANDLER_KEY = "location handler thread"
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Single<HashMap<String, Double>> {
        return Single.create {
            val locationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val handlerThread = HandlerThread(LOCATION_HANDLER_KEY)
            handlerThread.start()

            try {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    10f,
                    object : android.location.LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            val locationHash = HashMap<String, Double>()
                            if (location != null) {
                                locationHash[LATITUDE] = location.latitude
                                locationHash[LONGITUDE] = location.longitude
                                it.onSuccess(locationHash)
                            }
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                        }

                        override fun onProviderEnabled(provider: String?) {}

                        override fun onProviderDisabled(provider: String?) {}
                    },
                    handlerThread.looper
                )
            } catch (e: Throwable) {
                Timber.e(e)
                it.onError(e)
            } catch (e: IllegalArgumentException) {
                Timber.e(e)
                it.onError(e)
            }
        }
    }
}
package com.example.weatherexplorer.view.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherexplorer.MainActivity
import com.example.weatherexplorer.R
import com.example.weatherexplorer.model.City
import com.example.weatherexplorer.model.DayItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_fragment_layout.*
import org.koin.android.ext.android.inject

class HomeFragment : Fragment(), DaysAdapterListener {

    companion object {
        private const val LOCATION_PERMISSION = "Location permission"
        private const val LOCATION_SETTINGS = "Location settings"
        const val IMAGE_BASE_URL = "http://openweathermap.org/img/w/"
        const val IMAGE_URL_IMAGE_TYPE = ".png"
    }

    private val mViewModel by inject<HomeViewModel>()
    private var mCity: City? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycle.addObserver(mViewModel)

        update_button.setOnClickListener {
            callCurrentLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeToLiveData()
    }

    override fun onResume() {
        super.onResume()

        callCurrentLocation()
    }

    override fun onDayItemClicked(dayItem: DayItem) {
        setUpWidgets(dayItem, mCity!!)
    }

    private fun subscribeToLiveData() {
        mViewModel.mShowUpdateButton.observe(this, Observer {
            if (it) {
                showUpdateButton()
                hideDaysRecycler()
            }
        })

        mViewModel.mErrorMessageLiveData.observe(this, Observer {
            showErrorNotification(it)
        })

        mViewModel.mWeatherLiveData.observe(this, Observer {
            if (it != null && !it.dayItem.isNullOrEmpty() && it.city != null) {
                val dayList = it.dayItem
                mCity = it.city
                setUpWidgets(dayList[0], mCity!!)
                fillDaysRecyclerView(dayList)
            }
        })

        mViewModel.mProgressLiveData.observe(this, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        mViewModel.mProgressLiveData.observe(this, Observer {
            if (it) {
                showUpdateButton()
                hideDaysRecycler()
            }
        })
    }

    private fun callCurrentLocation() {
        if (isLocationPermissionGranted() && isLocationEnabled() && isInternetAvailable()) {
            mViewModel.getCurrentLocation()
            update_button.visibility = View.GONE
        } else {
            showUpdateButton()
            hideDaysRecycler()
        }
    }

    private fun isInternetAvailable(): Boolean {
        return if ((activity as MainActivity).isInternetAvailable()) {
            true
        } else {
            showErrorNotification(getString(R.string.turn_on_internet_connection_text))
            false
        }

    }

    private fun isLocationEnabled(): Boolean {
        return if ((activity as MainActivity).isLocationEnabled()) {
            true
        } else {
            showAskForLocationNotification(
                getString(R.string.enable_location_text),
                LOCATION_SETTINGS
            )
            false
        }
    }

    private fun showUpdateButton() {
        update_button.visibility = View.VISIBLE
    }

    private fun hideDaysRecycler() {
        days_recycler.visibility = View.GONE
    }

    private fun setUpWidgets(dayItem: DayItem, city: City) {
        setDegreeText(dayItem, city)
        showPressureViews()
        setPressureText(dayItem)
        showHumidityViews()
        setHumidityText(dayItem)
        showWindViews()
        setWindText(dayItem)
        setWeatherImage(dayItem)
    }

    private fun fillDaysRecyclerView(day: List<DayItem>) {
        days_recycler.visibility = View.VISIBLE
        days_recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        days_recycler.adapter = DaysRecyclerAdapter(day, this)
    }

    private fun setWeatherImage(dayItem: DayItem) {
        Picasso.Builder(requireContext()).build()
            .load(IMAGE_BASE_URL + dayItem.weather?.get(0)?.icon + IMAGE_URL_IMAGE_TYPE)
            .error(R.drawable.ic_error)
            .into(weather_image_view)
    }

    private fun setWindText(dayItem: DayItem) {
        wind_value_text.text =
            getString(R.string.wind_speed_value_text, dayItem.speed.toString())
    }

    private fun showWindViews() {
        wind_value_text.visibility = View.VISIBLE
        wind_title_text.visibility = View.VISIBLE
        wind_image_view.visibility = View.VISIBLE
    }

    private fun setHumidityText(dayItem: DayItem) {
        val humidity = dayItem.humidity.toString() + "%"
        humidity_value_text.text = humidity
    }

    private fun showHumidityViews() {
        humidity_value_text.visibility = View.VISIBLE
        humidity_title_text.visibility = View.VISIBLE
        humidity_image_view.visibility = View.VISIBLE
    }

    private fun setPressureText(dayItem: DayItem) {
        pressure_value_text.text =
            getString(R.string.pressure_value_text, dayItem.pressure.toString())
    }

    private fun showPressureViews() {
        pressure_value_text.visibility = View.VISIBLE
        pressure_title_text.visibility = View.VISIBLE
        pressure_image_view.visibility = View.VISIBLE
    }

    private fun setDegreeText(dayItem: DayItem, city: City) {
        degree_text.text = getString(
            R.string.main_degree_value_text,
            city.name,
            dayItem.temp?.eve.toString()
        )
    }

    private fun showProgress() {
        progress_circular.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress_circular.visibility = View.GONE
    }

    private fun showErrorNotification(message: String?) {
        if (!message.isNullOrEmpty()) {
            (activity as MainActivity).showErrorNotification(message)
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if ((activity as MainActivity).checkLocationPermissions()) {
            true
        } else {
            showAskForLocationNotification(
                getString(R.string.allow_location_permission_text),
                LOCATION_PERMISSION
            )
            false
        }
    }

    private fun showAskForLocationNotification(message: String?, intention: String) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.error_notification_layout, null)
        dialogBuilder.setView(view)
        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        view.findViewById<TextView>(R.id.error_description_text).text = message
        view.findViewById<TextView>(R.id.error_text).text = intention
        view.findViewById<ImageView>(R.id.dismiss_image).setOnClickListener {
            alertDialog.dismiss()
        }
        view.findViewById<ImageView>(R.id.error_image).visibility = View.GONE
        val okButton = view.findViewById<Button>(R.id.ok_button)
        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            alertDialog.dismiss()
        }
        okButton.setOnClickListener {
            alertDialog.dismiss()
            grantLocation(intention)
        }
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun grantLocation(intention: String) {
        when (intention) {
            LOCATION_PERMISSION -> {
                (activity as MainActivity).onRequestLocationPermission()
            }
            else -> {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requireContext().startActivity(intent)
            }
        }
    }
}
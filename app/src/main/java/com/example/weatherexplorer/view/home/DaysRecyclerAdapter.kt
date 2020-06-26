package com.example.weatherexplorer.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherexplorer.R
import com.example.weatherexplorer.model.DayItem
import com.example.weatherexplorer.utils.Utils.Companion.getDayOfWeek
import com.squareup.picasso.Picasso


class DaysRecyclerAdapter(
    private val day: List<DayItem>,
    private val daysAdapterListener: DaysAdapterListener
) :
    RecyclerView.Adapter<DaysRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val MONDAY = "Mon"
        private const val TUESDAY = "Tue"
        private const val WEDNESDAY = "Wen"
        private const val THURSDAY = "Thu"
        private const val FRIDAY = "Fri"
        private const val SATURDAY = "Sat"
        private const val SUNDAY = "Sun"
    }

    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.days_wether_item_layout,
                parent,
                false
            ),
            day,
            daysAdapterListener
        )
    }

    override fun getItemCount(): Int {
        return day.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayItem: DayItem? = day[position]

        if (dayItem != null) {
            setDayText(holder.dayText, dayItem)
            setDegreeText(holder.degreeText, dayItem)
            setWeatherImage(holder.weatherImageView, dayItem)
        }
    }

    private fun setDayText(dayText: TextView, dayItem: DayItem) {

        when (getDayOfWeek(dayItem.dt)) {
            1 -> {
                dayText.text = SUNDAY
            }
            2 -> {
                dayText.text = MONDAY
            }
            3 -> {
                dayText.text = TUESDAY
            }
            4 -> {
                dayText.text = WEDNESDAY
            }
            5 -> {
                dayText.text = THURSDAY
            }
            6 -> {
                dayText.text = FRIDAY
            }
            7 -> {
                dayText.text = SATURDAY
            }
        }
    }

    private fun setWeatherImage(weatherImageView: ImageView, dayItem: DayItem) {
        Picasso.Builder(mContext!!).build()
            .load(MainFragment.IMAGE_BASE_URL + dayItem.weather?.get(0)?.icon + MainFragment.IMAGE_URL_IMAGE_TYPE)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_error)
            .into(weatherImageView)
    }

    private fun setDegreeText(degreeText: TextView, dayItem: DayItem) {
        degreeText.text =
            mContext!!.resources.getString(
                R.string.item_degree_value_text,
                dayItem.temp?.eve.toString()
            )
    }

    inner class ViewHolder(
        view: View,
        day: List<DayItem>,
        daysAdapterListener: DaysAdapterListener
    ) : RecyclerView.ViewHolder(view) {
        val weatherImageView: ImageView = view.findViewById(R.id.weather_image_view)
        val degreeText: TextView = view.findViewById(R.id.degree_text)
        val dayText: TextView = view.findViewById(R.id.day_text)

        init {
            view.setOnClickListener {
                daysAdapterListener.onDayItemClicked(day[adapterPosition])
            }
        }
    }
}
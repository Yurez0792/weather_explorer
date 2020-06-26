package com.example.weatherexplorer.view.home

import com.example.weatherexplorer.model.DayItem

interface DaysAdapterListener {
    /**
     * Trigger in response to click on DaysRecycler item
     * @param dayItem - weather data for appropriate day
     */
    fun onDayItemClicked(dayItem: DayItem)
}
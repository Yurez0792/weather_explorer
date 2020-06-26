package com.example.weatherexplorer.utils

import java.util.*

class Utils {
    companion object {
        fun getDayOfWeek(unixTime: Long): Int {
            val cal = Calendar.getInstance()
            cal.time = Date(unixTime * 1000)
            return cal.get(Calendar.DAY_OF_WEEK)
        }
    }
}
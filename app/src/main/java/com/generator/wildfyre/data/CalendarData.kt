package com.generator.wildfyre.data

import java.text.SimpleDateFormat
import java.util.*

class CalendarData {

    fun getLastMonth(data : String): String {
        var date = Calendar.getInstance()
        var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        date.add(Calendar.DAY_OF_YEAR, -data.toInt())
        var finalDate = format.format(date.time)
        return finalDate
    }

    fun getCurrentDate(): String {
        var date = Calendar.getInstance()
        var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        var finalDate = format.format(date.time)
        return finalDate
    }

    fun processByDay() : String {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        var day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        return day
    }

    fun processByDate() : String {
        val sdf = SimpleDateFormat("dd")
        val currentDate = sdf.format(Date())
        return currentDate + formatDate(currentDate)
    }

    private fun formatDate(date : String) : String {
        val suffixes = mapOf(
            "1" to "st", "21" to "st", "31" to "st",
            "2" to "nd", "22" to "nd",
            "3" to "rd", "23" to "rd"
        )

        return suffixes[date] ?: "th"
    }

    fun processByTime() : String {
        val sdf = SimpleDateFormat("HH")
        val currentDate = sdf.format(Date())
        return currentDate
    }

}
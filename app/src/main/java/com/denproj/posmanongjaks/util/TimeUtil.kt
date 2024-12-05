package com.denproj.posmanongjaks.util

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

class TimeUtil {
    companion object {


        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        // Accurate Up To Minute
        fun getCurrentTime(): String {
            val startOfDay = LocalDateTime.now().atZone(ZoneId.systemDefault())
            return timeFormatter.format(startOfDay)
        }

        // YYYY-MM-D formatted
        fun getCurrentDate(): String {
            val startOfDay = LocalDateTime.now().atZone(ZoneId.systemDefault())
            return dateFormatter.format(startOfDay)
        }

        fun getCurrentDateTitleFormat(): String {
            val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")
            val date = LocalDateTime.now().atZone(ZoneId.systemDefault())
            // Format the date
            return date.format(formatter)
        }
    }
}


package ru.paswd.infosearch.wikidocumentlistcreator.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    fun getDateTime(): String {
        val calendar = Calendar.getInstance()
        return getDateTime(calendar)
    }

    fun getDateTime(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(calendar.time)
    }
}
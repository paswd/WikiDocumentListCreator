package ru.paswd.infosearch.wikidocumentlistcreator.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(calendar.time)
    }
}
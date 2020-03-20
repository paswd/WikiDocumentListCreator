package ru.paswd.infosearch.wikidocumentlistcreator.logger

import ru.paswd.infosearch.wikidocumentlistcreator.utils.DateTimeUtils

object Log {

    private const val PREFIX_INFO = "INFO"
    private const val PREFIX_WARN = "WARN"
    private const val PREFIX_ERROR = "ERROR"
    private const val PREFIX_DEBUG = "DEBUG"

    fun log(message: String, prefix: String) {
        println("${DateTimeUtils.getDateTime()} [$prefix]  $message")
    }

    fun info(message: String) {
        log(message, PREFIX_INFO)
    }

    fun warn(message: String) {
        log(message, PREFIX_WARN)
    }

    fun error(message: String) {
        log(message, PREFIX_ERROR)
    }

    fun debug(message: String) {
        log(message, PREFIX_DEBUG)
    }

}
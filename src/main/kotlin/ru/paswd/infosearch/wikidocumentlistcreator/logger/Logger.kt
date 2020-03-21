package ru.paswd.infosearch.wikidocumentlistcreator.logger

import ru.paswd.infosearch.wikidocumentlistcreator.utils.DateTimeUtils
import java.io.File

object Logger {

    private const val PREFIX_INFO = "INFO"
    private const val PREFIX_WARN = "WARN"
    private const val PREFIX_ERROR = "ERROR"
    private const val PREFIX_DEBUG = "DEBUG"

    private var file: File? = null

    fun log(message: String) = log(message, "")

    fun log(message: String, prefix: String) {
        var out = ""
        if (prefix.isNotEmpty())
            out += "${DateTimeUtils.getDateTime()} [$prefix]  "

        out += message + "\n"

        print(out)
        file?.appendText(out)
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

    fun setOutput(filename: String) {
        file = File(filename)
        file?.writeText("")
    }
}
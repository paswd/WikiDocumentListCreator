package ru.paswd.infosearch.wikidocumentlistcreator.utils

import org.jsoup.Jsoup

object StringUtils {
    fun convertJsonSpecialChars(json: String): String {
        return json
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\b", "\\b")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }

    fun removeMarkup(markup: String?): String {
        if (markup == null)
            return ""

        return Jsoup.parse(markup).text()
    }
}
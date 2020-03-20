package ru.paswd.infosearch.wikidocumentlistcreator.utils

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
}
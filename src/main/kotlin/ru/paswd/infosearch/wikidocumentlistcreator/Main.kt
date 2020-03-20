package ru.paswd.infosearch.wikidocumentlistcreator

import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnLongResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.utils.DateTimeUtils
import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

const val ROOT_TITLE = "Категория:Авиация"
const val DIR_PATH = "/output/"
const val FILE_PATH = "aviation.json"

fun main(args: Array<String>) {

    val absPath = Paths.get("").toAbsolutePath().toString()
    val dir = File(absPath + DIR_PATH)
    dir.mkdirs()
    val file = File(absPath + DIR_PATH + FILE_PATH)
    file.createNewFile()
    file.writeText("")

    val loader = DocumentLoader()
    val timeStart = Calendar.getInstance()

    loader.getAllChildren(ROOT_TITLE, file, 1, OnLongResultListener {
        //todo: сделать нормальный logger
        val timeEnd = Calendar.getInstance()
        onSuccess("$absPath$DIR_PATH$FILE_PATH", it, timeStart, timeEnd)

        exitProcess(0)
    })
}

fun onSuccess(filePath: String, count: Long, timeStart: Calendar, timeEnd: Calendar) {
    println("\n=========")
    println("[SUCCESS]")
    println("Wrote to file: $filePath")
    println("Total count: $count")
    println("Begin: ${DateTimeUtils.getDateTime(timeStart)}")
    println("End: ${DateTimeUtils.getDateTime(timeEnd)}")
}

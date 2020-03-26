package ru.paswd.infosearch.wikidocumentlistcreator

import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnLongResultListener
import ru.paswd.infosearch.wikidocumentlistcreator.logger.Logger
import ru.paswd.infosearch.wikidocumentlistcreator.utils.DateTimeUtils
import java.io.File
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

const val ROOT_TITLE = "Категория:Авиация"
const val DIR_PATH = "/output/"
const val FILE_PATH = "aviation.json"
const val LOG_PATH = "log.txt"

fun main(args: Array<String>) {

    val absPath = Paths.get("").toAbsolutePath().toString()
    val dir = File(absPath + DIR_PATH)
    dir.mkdirs()
    val file = File(absPath + DIR_PATH + FILE_PATH)
    file.createNewFile()
    file.writeText("")

    Logger.setOutput(absPath + DIR_PATH + LOG_PATH)

    val parser: GraphParser = GraphParserImpl()
    val timeStart = Calendar.getInstance()

    parser.parse(ROOT_TITLE, file, 0, OnLongResultListener {
        //todo: сделать нормальный logger
        val timeEnd = Calendar.getInstance()
        onSuccess("$absPath$DIR_PATH$FILE_PATH", it, timeStart, timeEnd)

        exitProcess(0)
    })
}

fun onSuccess(filePath: String, count: Long, timeStart: Calendar, timeEnd: Calendar) {
    Logger.log("\n=========")
    Logger.log("[SUCCESS]")
    Logger.log("Wrote to file: $filePath")
    Logger.log("Total count: $count")
    Logger.log("Begin: ${DateTimeUtils.getDateTime(timeStart)}")
    Logger.log("End: ${DateTimeUtils.getDateTime(timeEnd)}")
}

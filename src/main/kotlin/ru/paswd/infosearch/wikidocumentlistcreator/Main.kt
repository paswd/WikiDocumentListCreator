package ru.paswd.infosearch.wikidocumentlistcreator

import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnResultListener
import java.io.File
import java.nio.file.Paths
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
    file.writeText("[\n")

    val loader = TitleLoader()
    loader.getAllChildrenTitles(ROOT_TITLE, file, 0, OnResultListener {
        //todo: сделать нормальный logger
        file.appendText("\n]\n")
        println("Wrote to file: $absPath$DIR_PATH$FILE_PATH")
        onSuccess()

        exitProcess(0)
    })
}

fun onSuccess() {
    println("[SUCCESS]")
}

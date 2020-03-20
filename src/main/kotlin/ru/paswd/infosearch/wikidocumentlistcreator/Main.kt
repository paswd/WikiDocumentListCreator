package ru.paswd.infosearch.wikidocumentlistcreator

import java.io.File
import java.nio.file.Paths

const val ROOT_TITLE = "Категория:Авиация"
const val DIR_PATH = "/output/"
const val FILE_PATH = "log.txt"

fun main(args: Array<String>) {

    val absPath = Paths.get("").toAbsolutePath().toString()
    val dir = File(absPath + DIR_PATH)
    dir.mkdirs()
    val file = File(absPath + DIR_PATH + FILE_PATH)
    file.createNewFile()
    file.writeText("")

    val loader = TitleLoader()
    loader.getAllChildrenTitles(ROOT_TITLE, file, 1, TitleLoader.OnResultListener {
        println("Wrote to file: $absPath$DIR_PATH$FILE_PATH")
        onSuccess()
    })
}

fun onSuccess() {
    println("[SUCCESS]")
}

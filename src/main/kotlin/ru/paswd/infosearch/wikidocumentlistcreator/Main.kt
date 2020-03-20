package ru.paswd.infosearch.wikidocumentlistcreator

const val ROOT_TITLE = "Категория:Авиация"

fun main(args: Array<String>) {

    val loader = TitleLoader()
    loader.getAllChildrenTitles(ROOT_TITLE, 1, TitleLoader.OnResultListener {
        loader.result.forEach {
            println(it)
        }
    })
}



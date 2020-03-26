package ru.paswd.infosearch.wikidocumentlistcreator

import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnLongResultListener
import java.io.File

abstract class GraphParser {
    fun parse(root: String, file: File, listener: OnLongResultListener)
            = parse(root, file, -1, listener)

    abstract fun parse(root: String, file: File, depth: Int, listener: OnLongResultListener)
}
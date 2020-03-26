package ru.paswd.infosearch.wikidocumentlistcreator

import ru.paswd.infosearch.wikidocumentlistcreator.listeners.OnGotContentListener

interface DocumentLoader {
    fun load(pageId: Long, listener: OnGotContentListener)
}
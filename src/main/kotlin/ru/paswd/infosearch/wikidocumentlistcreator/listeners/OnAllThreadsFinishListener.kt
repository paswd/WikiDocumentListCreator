package ru.paswd.infosearch.wikidocumentlistcreator.listeners

class OnAllThreadsFinishListener(
    private val threads: Int,
    private val listener: OnResultListener
) {
    private var count: Int = 0

    fun append() {
        count++

        if (count >= threads)
            listener.onResult()
    }
}
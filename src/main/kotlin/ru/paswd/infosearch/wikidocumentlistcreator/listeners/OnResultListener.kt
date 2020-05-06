package ru.paswd.infosearch.wikidocumentlistcreator.listeners

interface OnResultListener {
    fun onResult()

    companion object {
        inline operator fun invoke(crossinline op: () -> Unit) =
            object : OnResultListener {
                override fun onResult() = op()
            }
    }
}
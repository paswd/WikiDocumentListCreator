package ru.paswd.infosearch.wikidocumentlistcreator.listeners

interface OnLongResultListener {
    fun onResult(l: Long)

    companion object {
        inline operator fun invoke(crossinline op: (l: Long) -> Unit) =
            object : OnLongResultListener {
                override fun onResult(l: Long) = op(l)
            }
    }
}
package ru.paswd.infosearch.wikidocumentlistcreator.listeners

interface OnStringResultListener {
    fun onResult(str: String)

    companion object {
        inline operator fun invoke(crossinline op: (str: String) -> Unit) =
            object : OnStringResultListener {
                override fun onResult(str: String) = op(str)
            }
    }
}
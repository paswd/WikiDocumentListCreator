package ru.paswd.infosearch.wikidocumentlistcreator.listeners

interface OnGotContentListener {
    fun onResult(title: String, json: String)

    companion object {
        inline operator fun invoke(crossinline op: (title: String, json: String) -> Unit) =
            object : OnGotContentListener {
                override fun onResult(title: String, json: String) = op(title, json)
            }
    }
}
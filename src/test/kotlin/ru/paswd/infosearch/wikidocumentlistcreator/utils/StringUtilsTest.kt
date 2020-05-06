package ru.paswd.infosearch.wikidocumentlistcreator.utils

import org.junit.Assert
import org.junit.Test

class StringUtilsTest {
    @Test
    fun testConvertJsonToSpecialChars() {
        //todo
    }

    @Test
    fun removeMarkup() {
        Assert.assertEquals("HelloWorld", StringUtils.removeMarkup("<html><body>HelloWorld</body></html>"))
        Assert.assertEquals("Hello, World!", StringUtils.removeMarkup("Hello, World!"))
    }
}
package org.dictat.wikistat.utils

import org.junit.Test
import org.junit.Assert

class StringUtilsTest {
    Test fun decode() {
        println(StringUtils.decodeString("tele\\xC4\\x8Dek"))
        println(StringUtils.decodeString("(VI.)_Henrik_n\\xC3\\xA9met_kir\\xC3\\xA1ly"))
    }
    Test fun removeSlashes() {
        Assert.assertEquals("Foo", StringUtils.removeSlashes("\t/Foo") )
        Assert.assertEquals("Foo", StringUtils.removeSlashes("/\tFoo") )
    }
}
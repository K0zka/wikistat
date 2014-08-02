package org.dictat.wikistat.wikidata

import org.junit.Test
import org.junit.Assert
import org.dictat.wikistat.wikidata.WikiData.PageMissingException

public class WikiDataTest {
    Test fun testLangLinks() {
        Assert.assertNotNull(WikiData().langLinks("Monday", "en"));
    }

    Test(expected = javaClass<PageMissingException>())
            fun testLangLinksNotexisting() {
        Assert.assertNotNull(WikiData().langLinks("1980 UFO attack on World Trade Center", "en"));
    }
}
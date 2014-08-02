package org.dictat.wikistat.services

import org.junit.Test
import org.mockito.Mockito
import java.text.SimpleDateFormat
import org.junit.Assert
import org.dictat.wikistat.model.PageActivity
import org.dictat.wikistat.model.TimeFrame

public class SummarizerTest {
    Test fun handlePage() {
        val sdf = SimpleDateFormat("yyyy-mm-dd HH")
        val pageDao = Mockito.mock(javaClass<PageDao>())!!
        val statDao = Mockito.mock(javaClass<StatDao>())!!
        val novaDao = Mockito.mock(javaClass<NovaDao>())!!
        val pageActivity = PageActivity()
        pageActivity.time = sdf.parse("2010-01-01 09")
        pageActivity.activity = 5
        Mockito.`when`(pageDao.getPageActivity("Jeno Kakukk","en"))!!.thenReturn(listOf(pageActivity))
        val summarizer = Summarizer(pageDao, statDao, novaDao)
        summarizer.handlePage("Jeno Kakukk","en", sdf.parse("2010-01-01 00")!!, sdf.parse("2010-01-02 23")!!)

    }
    Test fun sum() {
        val pageDao = Mockito.mock(javaClass<PageDao>())!!
        val statDao = Mockito.mock(javaClass<StatDao>())!!
        val novaDao = Mockito.mock(javaClass<NovaDao>())!!
        val summarizer = Summarizer(pageDao, statDao, novaDao)
        Assert.assertEquals(10, summarizer.sum(listOf( 1, 2, 3, 4), {it}))
        Assert.assertEquals(0, summarizer.sum(listOf( 0 ), {0}))
    }
}

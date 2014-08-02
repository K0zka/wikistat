package org.dictat.wikistat.services

import java.util.Date
import org.junit.Test
import org.mockito.Mockito
import org.mockito.internal.stubbing.answers.DoesNothing
import org.junit.Ignore
import org.slf4j.LoggerFactory
import java.util.ArrayList
import org.junit.Assert
import org.dictat.wikistat.model.TimeFrame

public class DumpProcessorTest {
    object constants {
        val logger = LoggerFactory.getLogger(javaClass)!!
    }
    Test fun getUrlForTime()  {
        val mock = Mockito.mock(javaClass<PageDao>())!!
        val statMock = Mockito.mock(javaClass<StatDao>())!!
        constants.logger.info(DumpProcessor(mock, statMock).getUrlForTime(Date()));
    }
    Ignore Test fun process(): Unit {
        val mock = Mockito.mock(javaClass<PageDao>())!!
        Mockito.`when`(mock.savePageEvent("", "", 1, Date(), TimeFrame.Hour, false))!!.then(DoesNothing())
        val statMock = Mockito.mock(javaClass<StatDao>())!!
        //Mockito.`when`(statMock.saveStatistics())!!.then(DoesNothing())
        DumpProcessor(mock, statMock).process("http://dumps.wikimedia.org/other/pagecounts-raw/2013/2013-01/pagecounts-20130126-050001.gz", Date());
    }
    Test fun filter() {
        val mock = Mockito.mock(javaClass<PageDao>())!!
        val proc = DumpProcessor(mock, Mockito.mock(javaClass<StatDao>())!!)

        Assert.assertTrue(proc.filter("hu"))
        Assert.assertTrue(proc.filter("en"))
        Assert.assertTrue(proc.filter("de"))

        val list = ArrayList<String>();
        list.add("hu.*");
        list.add("en.*");
        proc.filters = list;

        Assert.assertTrue(proc.filter("hu"))
        Assert.assertTrue(proc.filter("en"))
        Assert.assertFalse(proc.filter("de"))

    }
}
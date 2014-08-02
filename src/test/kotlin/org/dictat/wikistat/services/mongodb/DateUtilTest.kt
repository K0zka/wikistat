package org.dictat.wikistat.services.mongodb

import org.junit.Test
import java.util.Date
import org.junit.Assert
import org.dictat.wikistat.model.TimeFrame
import java.text.SimpleDateFormat

public class DateUtilTest {
    Test fun intToDate() {
        DateUtil.intToDate(0, TimeFrame.Hour)
    }
    Test fun dateToInt() {
        DateUtil.dateToInt(Date(), TimeFrame.Hour)
    }
    Test fun dateToIntCheck() {
        val now = Date()
        val shortened = DateUtil.dateToInt(now, TimeFrame.Hour)
        val truncated = DateUtil.intToDate(shortened, TimeFrame.Hour)
    }
    Test fun intToString() {
        Assert.assertEquals("1", DateUtil.intToString(1));
        Assert.assertEquals("10", DateUtil.intToString(76));
        Assert.assertEquals("20", DateUtil.intToString(152));
        Assert.assertEquals("21", DateUtil.intToString(153));
        Assert.assertEquals("100", DateUtil.intToString(5776));
        Assert.assertEquals("101", DateUtil.intToString(5777));
    }
    Test fun intToStringBackMap() {
        for(i in 1..100000) {
            Assert.assertEquals(i, DateUtil.stringToInt(DateUtil.intToString(i)))
        }
    }
    Test fun stringToInt() {
        Assert.assertEquals(0, DateUtil.stringToInt("0"))
        Assert.assertEquals(76, DateUtil.stringToInt("10"))
        Assert.assertEquals(76, DateUtil.stringToInt("10"))
        Assert.assertEquals(77, DateUtil.stringToInt("11"))
        Assert.assertEquals(78, DateUtil.stringToInt("12"))
        Assert.assertEquals(152, DateUtil.stringToInt("20"))
        Assert.assertEquals(153, DateUtil.stringToInt("21"))
        Assert.assertEquals(1, DateUtil.stringToInt("01"))
    }
    Test fun charToInt() {
        println("a\t" + DateUtil.charToInt('a'))
        println("#\t" + DateUtil.charToInt('#'))
        println("0\t" + DateUtil.charToInt('0'))
        println("$\t" + DateUtil.charToInt('$'))
    }
    Test fun charBackMap() {
        for(i in 0..DateUtil.nrDigits - 1) {
            Assert.assertEquals(i, DateUtil.charToInt(DateUtil.intToChar(i)))
        }
    }
    Test(expected=javaClass<IllegalArgumentException>()) fun illegalCharacters() {
        DateUtil.charToInt('á')
    }
    Test fun dateToStr() {
        println(DateUtil.dateToInt(Date(), TimeFrame.Hour));
        println(DateUtil.intToString(DateUtil.dateToInt(Date(), TimeFrame.Hour)));
        println(DateUtil.intToString(DateUtil.dateToInt(Date(), TimeFrame.Day)));
        println(DateUtil.intToString(DateUtil.dateToInt(Date(), TimeFrame.Week)));
    }
    Test fun dateToStrAndBack() {
        println(DateUtil.dateToString(Date(), TimeFrame.Hour))
        println(DateUtil.stringToDate(DateUtil.dateToString(Date(), TimeFrame.Hour), TimeFrame.Hour))

        println(DateUtil.dateToString(Date(), TimeFrame.Day))
        println(DateUtil.stringToDate(DateUtil.dateToString(Date(), TimeFrame.Day), TimeFrame.Day))

        println(DateUtil.dateToString(Date(), TimeFrame.Week))
        println(DateUtil.stringToDate(DateUtil.dateToString(Date(), TimeFrame.Week), TimeFrame.Week))

        println(DateUtil.dateToString(Date(), TimeFrame.Month))
        println(DateUtil.stringToDate(DateUtil.dateToString(Date(), TimeFrame.Month), TimeFrame.Month))
    }
    Test fun dateToIntAndBack() {
        println(DateUtil.dateToInt(Date(), TimeFrame.Hour))
        println(DateUtil.intToDate(DateUtil.dateToInt(Date(), TimeFrame.Hour), TimeFrame.Hour))
    }
    Test fun datesBetween() {
        val df = SimpleDateFormat("yyyy-mm-dd HH")
        val ret = DateUtil.datesBetween(df.parse("2010-01-01 01")!!, df.parse("2010-01-01 12")!!, TimeFrame.Hour)
        Assert.assertNotNull(ret)
        Assert.assertFalse(ret.isEmpty())
    }
}
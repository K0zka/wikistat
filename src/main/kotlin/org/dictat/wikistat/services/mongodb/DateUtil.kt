package org.dictat.wikistat.services.mongodb

import java.util.Date
import org.dictat.wikistat.model.TimeFrame
import java.util.GregorianCalendar
import java.util.Calendar

/**
 * Utility class to represent dates as short strings, this helps having compact data-representation in mongodb as
 * mongodb keys must be string
 */
object DateUtil {
    private val baseDateObj = Date(107, 0, 1)
    private val baseDate = baseDateObj.getTime()
    private val hour = 1000 * 60 * 60
    private val day = 24 * hour
    private val week = 7 * day
    fun dateToInt(date: Date, tf: TimeFrame): Int {
        when(tf) {
            TimeFrame.Hour -> return ((date.getTime() - baseDate) / hour).toInt();
            TimeFrame.Day -> return ((date.getTime() - baseDate) / day).toInt();
            TimeFrame.Week -> return ((date.getTime() - baseDate) / week).toInt();
            TimeFrame.Month -> return (monthSinceBaseDate(date)).toInt();
            else -> {
                throw IllegalArgumentException()
            }
        }

    }

    fun monthSinceBaseDate(date: Date): Int {
        val cal = GregorianCalendar()
        cal.setTime(date)
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        return ((year - 2007) * 12) + month
    }

    fun intToDate(n: Int, tf: TimeFrame): Date {
        when(tf) {
            TimeFrame.Hour -> return Date((n.toLong() * hour) + baseDate)
            TimeFrame.Day -> return Date((n.toLong() * day) + baseDate)
            TimeFrame.Week -> return Date((n.toLong() * week) + baseDate)
            TimeFrame.Month -> {
                val cal = GregorianCalendar()
                cal.setTimeInMillis(baseDate)
                cal.add(Calendar.MONTH, n)
                return cal.getTime()
            };
            else -> throw IllegalArgumentException()
        }

    }

    //holy constant! do not touch!
    private val digits = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-~_^!/:#?+%*$".toCharArray()
    val nrDigits = digits.size

    fun dateToString(date: Date, tf: TimeFrame): String {
        return intToString(dateToInt(date, tf));
    }

    fun stringToDate(str: String, tf: TimeFrame): Date {
        return intToDate(stringToInt(str), tf)
    }

    fun intToString(intgr: Int): String {
        var value = intgr
        val builder = StringBuilder()
        for(i in 1..Integer.MAX_VALUE) {
            val loc = value % nrDigits;
            builder.insert(0, intToChar(loc))
            value = (value - loc) / nrDigits
            if(value == 0) {
                return builder.toString()
            }
        }
        return builder.toString()
    }
    fun stringToInt(str: String): Int {
        var value = 0;
        str.forEach { value = (value * nrDigits) + charToInt(it) }
        return value;
    }
    fun intToChar(i: Int): Char {
        if(i < nrDigits && i > -1) {
            return digits[i]
        } else {
            throw IllegalArgumentException("Not good: " + i)
        }
    }
    fun charToInt(chr: Char): Int {
        //this might be braindead
        for(i in 0..nrDigits - 1) {
            if (chr == digits[i])
                return i;
        }
        throw IllegalArgumentException("Evil digit!");
    }
    fun datesBetween(start: Date, end: Date, tf: TimeFrame): List<String> {
        val ret = listBuilder<String>()
        for(i in dateToInt(start, tf)..dateToInt(end, tf) - 1) {
            ret.add(intToString(i))
        }
        return ret.build()
    }
}
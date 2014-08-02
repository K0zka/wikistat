package org.dictat.wikistat.services

import org.slf4j.LoggerFactory
import java.util.Date
import org.dictat.pump4j.CompressionUtil
import java.net.URL
import org.dictat.pump4j.Compression
import java.util.TreeMap
import java.io.InputStreamReader
import java.net.URLDecoder
import org.dictat.wikistat.model.TimeFrame
import java.text.SimpleDateFormat
import java.util.Timer
import java.io.FileNotFoundException
import org.apache.commons.lang.time.DateUtils
import java.util.concurrent.atomic.AtomicLong
import org.apache.commons.lang.StringEscapeUtils
import org.dictat.wikistat.utils.StringUtils
import java.io.IOException

abstract class AbstractDumpProcessor (val pageDao: PageDao, val statDao: StatDao) {
    val timer = Timer()

    private object constants {
        val logger = LoggerFactory.getLogger(javaClass<AbstractDumpProcessor>())!!
    }
    var filters: List<String>? = null;
    fun filter(lang: String): Boolean {
        if (filters == null) {
            return true
        } else {
            return filters!!.contains(lang)
        }
    }

    private fun processUrl(url : String, time : Date) : Boolean {
        var cntr = 0;
        while(cntr < 100) {
            try {
                process(url, time)
                return true
            } catch(http404: FileNotFoundException) {
                //tolerate missing dumps
                constants.logger.info("404 for $url - trying next")
            } catch(ioe : IOException) {
                constants.logger.info("403 for $url - trying again", ioe)
                Thread.sleep(1000);
            }
        }
        return false;
    }

    fun process(time: Date) {

        for(i in 0..100) {
            val url = getUrlForTime(DateUtils.addSeconds(time, i)!!);
            if(processUrl(url, time)) {
                break;
            }
        }
    }

    fun process(url: String, time: Date) {
        constants.logger.info("Loading: " + url)
        val langTotals = TreeMap<String, Long>()
        if(isLoaded(time)) {
            constants.logger.info("Already loaded: " + time)
            return
        }

        val total = AtomicLong(0)
        CompressionUtil.decompressor (URL(url).openStream(), Compression.Gzip, 1000000)!!.use {
            InputStreamReader(it).forEachLine {
                val fields: Array<String> = it.split(" ")
                if (fields.size != 4) {
                    constants.logger.warn("Malformated line: " + it)
                } else {
                    try {
                        val lang: String = fields[0]
                        val cnt: Long = fields[2].toLong()
                        if (filter(lang)) {
                            val page = StringUtils.removeSlashes(StringUtils.decodeString(fields[1]));

                            if (page.length() < 128) {
                                pageDao.savePageEvent(page, lang, cnt.toInt(), time, TimeFrame.Hour, false)
                            }
                        }
                        val oldVal: Long = (langTotals.get(lang) ?: 0)
                        langTotals.put(lang, oldVal + cnt)
                        total.addAndGet(cnt)
                    } catch (e: IllegalArgumentException) {
                        constants.logger.trace(e.getMessage(), e)
                    }
                }
            }
        }
        constants.logger.info("Saving totals: " + url)
        statDao.saveStatistics(langTotals, total.get(), time)
        constants.logger.info("Finished: " + url)
    }



    fun getUrlForTime(time: Date): String {
        return SimpleDateFormat("'http://dumps.wikimedia.org/other/pagecounts-raw/'yyyy'/'yyyy'-'MM'/pagecounts-'yyyyMMdd'-'HHmmss'.gz'").format(time);
    }

    fun isLoaded(time: Date): Boolean {
        return statDao.isLoaded(time)
    }

}

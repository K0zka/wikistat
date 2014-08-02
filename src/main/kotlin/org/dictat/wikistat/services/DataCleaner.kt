package org.dictat.wikistat.services

import org.dictat.wikistat.wikidata.WikiData
import java.util.Date
import java.util.TimerTask
import org.dictat.pump4j.system.SystemGzipInputStream
import java.net.URL
import java.io.InputStreamReader
import org.slf4j.LoggerFactory

public class DataCleaner(pageDao: PageDao, statDao: StatDao, val wiki: WikiData): AbstractDumpProcessor(pageDao, statDao) {
    object constants {
        val logger = LoggerFactory.getLogger(javaClass<DataCleaner>())!!
    }
    inner class Daily(): TimerTask() {
        public override fun run() {
            for(lang in pageDao.getLangs()) {
                constants.logger.info("marking all valid pages for ${lang}")
                SystemGzipInputStream(
                        URL("http://dumps.wikimedia.org/$lang/wiki/latest/$lang/wiki-latest-all-titles-in-ns0.gz")
                        .openConnection()!!.getInputStream()).use {

                    InputStreamReader(it).forEachLine {
                        pageDao.markChecked(it.replaceAll("_", " "), lang)
                    }
                }
                constants.logger.info("marked all valid pages for ${lang}")
            }
        }
    }
    fun start() {
        timer.schedule(Daily(), Date(), 14 * 24 * 60 * 60 * 1000.toLong());
    }
}
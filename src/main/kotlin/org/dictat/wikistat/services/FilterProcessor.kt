package org.dictat.wikistat.services

import java.util.concurrent.ExecutorService
import org.dictat.wikistat.utils.submit
import org.dictat.wikistat.wikidata.WikiData
import org.dictat.wikistat.wikidata.WikiData.PageMissingException
import org.slf4j.LoggerFactory
import java.util.TimerTask
import java.util.Date

public class FilterProcessor(pageDao: PageDao, statDao: StatDao, val httpExecutor: ExecutorService, val wiki: WikiData): AbstractDumpProcessor(pageDao, statDao) {

    class Task(val proc: FilterProcessor): TimerTask() {
        public override fun run() {
            proc.process();
        }
    }

    private object constants {
        val logger = LoggerFactory.getLogger(javaClass<FilterProcessor>())!!
    }
    fun process() {
        for (lang in pageDao.getLangs()) {
            val pageNames = pageDao.findUnchecked(lang)
            try {
                for (page in pageNames) {
                    check(page, lang);
                }
            } finally {
                pageNames.close()
            }
        }
    }
    fun check(page: String, lang: String) {
        //TODO all tasks with the same priority, does not make much sense
        httpExecutor.submit({
            try {
                wiki.langLinks(page, lang);
                constants.logger.debug("page OK $page $lang")
                pageDao.markChecked(page, lang)
            } catch (e: PageMissingException) {
                constants.logger.debug("removing $page $lang")
                pageDao.removePage(page, lang)
            }
        }, 1)
    }
    fun start() {
        constants.logger.info("Starting filter");
        timer.schedule(Task(this), Date(), 7 * 24 * 60 * 60 * 1000.toLong());
    }
}
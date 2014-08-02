package org.dictat.wikistat.services.async

import java.util.Date
import java.util.concurrent.ExecutorService
import org.dictat.wikistat.model.PageActivity
import org.dictat.wikistat.model.TimeFrame
import org.dictat.wikistat.utils.LazyIterable
import org.dictat.wikistat.utils.submit
import org.slf4j.LoggerFactory
import org.dictat.wikistat.services.PageDao

public class AsyncPageDao (val pageDao: PageDao, val executor: ExecutorService): PageDao {
    override fun findExtraordinaryActivity(lang: String, date: Date): Iterable<String> {
        return executor.submit( { pageDao.findExtraordinaryActivity(lang, date) }, Priority.updatePriority ).get()!!
    }
    object constants {
        val logger = LoggerFactory.getLogger(javaClass<AsyncPageDao>())!!
    }
    override fun replaceSum(page: String, lang: String, from: TimeFrame, to: TimeFrame, fromTime: Date, toTime: Date, sum: Int) {
        executor.submit( { pageDao.replaceSum(page, lang, from, to, fromTime, toTime, sum) } , 1 )
    }
    override fun markChecked(page: String, lang: String) {
        executor.submit({ pageDao.markChecked(page, lang) }, Priority.updatePriority)
    }
    override fun findUnchecked(lang: String): LazyIterable<String> {
        return pageDao.findUnchecked(lang)
    }
    override fun removePage(page: String, lang: String) {
        executor.submit({ pageDao.removePage(page, lang) }, Priority.removePriority)
    }
    override fun getPageNames(lang: String): LazyIterable<String> {
        return executor.submit({ pageDao.getPageNames(lang) }, Priority.readPriority).get()!!
    }
    override fun removePageEvents(name: String, lang: String, times: List<Pair<Date, TimeFrame>>) {
        executor.submit({ pageDao.removePageEvents(name, lang, times) }, Priority.updatePriority)
    }
    override fun getLangs(): List<String> {
        return pageDao.getLangs()
    }

    override fun savePageEvent(name: String, lang: String, cnt: Int, time: Date, timeFrame: TimeFrame, special: Boolean) {
        executor.submit({ pageDao.savePageEvent(name, lang, cnt, time, timeFrame, special) }, Priority.updatePriority)
    }
    override fun findPages(prefix: String, lang: String, max: Int): List<String> {
        return executor.submit({ pageDao.findPages(prefix, lang, max) }, Priority.readPriority).get()!!;
    }
    override fun getPageActivity(name: String, lang: String): List<PageActivity> {
        val future = executor.submit({
            pageDao.getPageActivity(name, lang)
        }, Priority.readPriority);
        return future.get()!!
    }



}
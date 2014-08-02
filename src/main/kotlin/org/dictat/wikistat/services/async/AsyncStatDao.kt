package org.dictat.wikistat.services.async

import java.util.concurrent.ExecutorService
import java.util.Date
import org.dictat.wikistat.model.PageActivity
import org.slf4j.LoggerFactory
import org.dictat.wikistat.utils.submit
import org.dictat.wikistat.services.StatDao
import org.dictat.wikistat.model.TimeFrame

public class AsyncStatDao(val statDao: StatDao, val executor: ExecutorService): StatDao {
    override fun setDateCompression(date: Date, tf: TimeFrame) {
        executor.submit( { statDao.setDateCompression(date, tf) } , Priority.updatePriority )
    }
    override fun getDateCompression(date: Date): TimeFrame? {
        return executor.submit( { statDao.getDateCompression(date) } , Priority.readPriority).get()
    }
    object constants {
        val logger = LoggerFactory.getLogger(javaClass<AsyncStatDao>())!!
    }
    override fun isLoaded(date: Date): Boolean {
        return executor.submit({ statDao.isLoaded(date) }, Priority.readPriority).get()!!
    }
    override fun saveStatistics(wikiact: Map<String, Long>, total: Long, date: Date) {
        executor.submit({ statDao.saveStatistics(wikiact, total, date) }, Priority.updatePriority)
    }
    override fun getStatistics(site: String): List<PageActivity> {
        return executor.submit({ statDao.getStatistics(site) } , Priority.readPriority).get()!!
    }
}
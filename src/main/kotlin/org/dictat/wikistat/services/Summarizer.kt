package org.dictat.wikistat.services

import org.slf4j.LoggerFactory
import java.util.Date
import java.util.TimerTask
import java.util.GregorianCalendar
import org.apache.commons.lang.time.DateUtils
import java.util.Calendar
import org.dictat.wikistat.model.TimeFrame
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

class Weekly(val summarizer: Summarizer): TimerTask() {
    public override fun run() {
        summarizer.weeklySummary();
    }
}

class Monthly(val summarizer: Summarizer): TimerTask() {
    public override fun run() {
        summarizer.monthlySummary();
    }
}

class Daily(val summarizer: Summarizer): TimerTask() {
    public override fun run() {
        summarizer.dailySummary();
    }
}


public class Summarizer(pageDao: PageDao, statDao: StatDao, val nova: NovaDao): AbstractDumpProcessor(pageDao, statDao) {

    object constants {
        val logger = LoggerFactory.getLogger(javaClass<Summarizer>())!!
    }

    /**
     * If a page's average activity is under this value, then it's exceptional values will not be stored.
     */
    var minimalExceptionalAverage = 20

    /**
     * calculate the average of the last N events
     */
    var averageOverLastN = 5

    /**
     * If the pags's activity is over the average by this percentage, then it is considered an exception and spared.
     */
    var exceptionExplosionPercent = 30

    fun weeklySummary() {
        for (lang in pageDao.getLangs()) {
            pageDao.getPageNames(lang).use {
                it.forEach {
//                    val activity = pageDao.getPageActivity(it, lang)
//                    constants.logger.info(it);
                }
            }
        }
    }

    fun dailySummary() {
        val now = Date()
        val targetDay = DateUtils.truncate(DateUtils.addDays(now, -8), Calendar.DAY_OF_MONTH)!!
        val targetDayEnd = DateUtils.addDays(targetDay, 1)!!
        for(day in 0..7) {
            for(hour in 0..24) {
                try {
                    val hourDate = DateUtils.addDays(DateUtils.addHours(targetDay, hour), day)!!
                    if(!statDao.isLoaded(hourDate)) {
                        constants.logger.info("Loading missing dump " + hour)
                        process(hourDate)
                    }
                } catch (e : IOException) {

                }
            }
        }
        constants.logger.info("Summarizing $targetDay")
        for(lang in pageDao.getLangs()) {
            constants.logger.info("\t-finding $lang")
            pageDao.getPageNames(lang).use {
                constants.logger.info("\t-checking $lang")
                val cntr = AtomicInteger()
                it.forEach { page ->
                    handlePage(page, lang, targetDay, targetDayEnd)
                    val inc = cntr.incrementAndGet()
                    if(inc % 10000 == 0) {
                        constants.logger.info("\t\t-$inc")
                    }
                }
                constants.logger.info("\t-done with $lang")
            }
        }
        constants.logger.info("Setting all hours for $targetDay day as compressed for day")
        for(hour in 0..24) {
            statDao.setDateCompression(DateUtils.addHours(targetDay, hour)!!, TimeFrame.Day)
        }
        constants.logger.info("Donw summarizing $targetDay")
    }

    fun sum<T>(list: List<T>, valfn: (T) -> Int): Int {
        var sum: Int = 0
        for(item in list) {
            sum = sum + valfn(item)
        }
        return sum;
    }

    fun getHour(date: Date): Short {
        val cal = GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY).toShort()
    }

    fun handlePage(page: String, lang: String, targetDay: Date, targetDayEnd: Date) {
        val activity = pageDao.getPageActivity(page, lang)

        val totalOnDay = sum(activity.filter { it.time?.before(targetDayEnd)!! && it.time?.after(targetDay)!! }, { it.activity!! })

        val totalInterval = sum(activity.filter { it.time?.before(DateUtils.addDays(targetDay, 7)!!)!! && it.time?.after(targetDay)!! }, { it.activity!! });
        val hourlyAvgs = IntArray(24)
        for(hour in 0..23) {
            hourlyAvgs[hour] = sum(activity.filter { getHour(it.time!!) == hour.toShort() }, { it.activity }) / 7
        }
//        val averageInterval = totalInterval.toDouble() / 7;

        if(totalOnDay != 0) {
            pageDao.replaceSum(page, lang, TimeFrame.Hour, TimeFrame.Day, targetDay, targetDayEnd, totalOnDay)
        }
        //nova.
    }

    fun monthlySummary() {

    }

    fun start() {
        timer.schedule(Daily(this), Date(), 24 * 60 * 60 * 1000.toLong())
    }

}
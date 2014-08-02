package org.dictat.wikistat.services

import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import org.slf4j.LoggerFactory
import java.util.TimerTask

public class DumpProcessor (pageDao: PageDao, statDao: StatDao): AbstractDumpProcessor(pageDao, statDao) {

    class Task(val proc: DumpProcessor): TimerTask() {
        public override fun run() {
            proc.process()
        }
    }

    private object constants {
        val logger = LoggerFactory.getLogger(javaClass<DumpProcessor>())!!;
    }
    fun process() {
        val cal = GregorianCalendar()
        cal.setTime(Date())
        cal.add(Calendar.HOUR, -4);
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        if(isLoaded(cal.getTime())) {
            constants.logger.info("Already loaded, skipping ${cal.getTime()}");
            return
        }
        for (i in 0..59) {
            cal.set(Calendar.SECOND, i)
            val time = cal.getTime()
            try {
                process(getUrlForTime(time), time)
                return
            } catch (e: IOException) {
                constants.logger.info(e.getMessage(), e);
            }
        }
    }

    fun start() {
        constants.logger.info("Starting dump processor");
        timer.schedule(Task(this), Date(), 60 * 60 * 1000.toLong());
    }

    fun stop() {
        timer.cancel();
    }
}
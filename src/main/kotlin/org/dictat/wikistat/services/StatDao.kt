package org.dictat.wikistat.services

import java.util.Date
import org.dictat.wikistat.model.PageActivity
import org.dictat.wikistat.model.TimeFrame

public trait StatDao {
    fun isLoaded(date : Date) : Boolean;
    fun saveStatistics(wikiact : Map<String, Long>, total: Long, date : Date)
    fun getStatistics(site : String) : List<PageActivity>
    fun getDateCompression(date : Date) : TimeFrame?
    fun setDateCompression(date : Date, tf : TimeFrame)
}
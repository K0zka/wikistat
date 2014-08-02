package org.dictat.wikistat.services

import java.util.Date
import org.dictat.wikistat.model.PageActivity
import org.dictat.wikistat.model.TimeFrame
import org.dictat.wikistat.utils.LazyIterable

public trait PageDao {
    fun getPageNames(lang : String): LazyIterable<String>
    fun savePageEvent(name : String, lang : String, cnt : Int, time : Date, timeFrame : TimeFrame, special : Boolean)
    fun removePageEvents(name : String, lang : String,times : List<Pair<Date, TimeFrame>>)
    fun getPageActivity(name: String, lang : String) : List<PageActivity>
    fun findPages(prefix : String, lang : String, max : Int) : List<String>
    fun getLangs() : List<String>
    fun removePage(page : String, lang : String) : Unit
    fun markChecked(page: String, lang : String) : Unit
    fun findUnchecked(lang : String) : LazyIterable<String>
    /**
     * Replace the activity with its sum
     */
    fun replaceSum(page: String, lang : String, from : TimeFrame, to : TimeFrame, fromTime : Date, toTime : Date, sum : Int) : Unit
    fun findExtraordinaryActivity(lang: String, date : Date): Iterable<String>
}
package org.dictat.wikistat.services.mongodb

import org.dictat.wikistat.services.StatDao
import java.util.Date
import com.mongodb.DB
import com.mongodb.BasicDBObject
import org.dictat.wikistat.model.PageActivity
import java.util.ArrayList
import com.mongodb.DBObject
import org.dictat.wikistat.model.TimeFrame
import java.util.Collections
import com.mongodb.DBCollection

public class MongoStatDaoImpl(db: DB): AbstractMongoDao(db), StatDao {
    override fun setDateCompression(date: Date, tf: TimeFrame) {
        getCollection().update(
                BasicDBObject("_id", DateUtil.dateToString(date, TimeFrame.Hour)),
                BasicDBObject("c",tf.symbol))
    }
    override fun getDateCompression(date: Date): TimeFrame? {
        val result = getCollection().find(BasicDBObject("_id", DateUtil.dateToString(date, TimeFrame.Hour)), BasicDBObject("c",1))!!
        val obj = result.next()
        return TimeFrame.Day
    }
    override fun getCollectionNames(): List<String> {
        return Collections.singletonList("totals");
    }
    override fun getIndices(): List<String> {
        return Collections.emptyList()
    }
    protected fun getCollection() : DBCollection {
        return db.getCollection("totals")!!
    }
    override fun getStatistics(site: String): List<PageActivity> {
        val stats = ArrayList<PageActivity>()
        for (x in getCollection().find(BasicDBObject(), BasicDBObject("e." + site, 1))) {
            val allSites = x.get("e") as DBObject
            if(allSites.containsField(site)) {
                val activity = PageActivity()
                activity.time = DateUtil.stringToDate(x.get("_id") as String, TimeFrame.Hour)
                activity.activity = allSites.get(site) as Int

                stats.add(activity)
            }
        }
        return stats
    }
    override fun isLoaded(date: Date): Boolean {
        return db.getCollection("totals")!!.find(BasicDBObject("_id", DateUtil.dateToString(date, TimeFrame.Hour)))!!.count() > 0;
    }
    override fun saveStatistics(wikiact: Map<String, Long>, total: Long, date: Date) {
        val stats = BasicDBObject()
        for (x in wikiact) {
            stats.append(x.component1().replaceAll("\\.", "_"), x.component2().toInt())
        }
        val doc = BasicDBObject("_id", DateUtil.dateToString(date, TimeFrame.Hour)).append("e", stats)!!.append("t", total);

        getCollection().insert(doc)
    }
}
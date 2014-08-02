package org.dictat.wikistat.services.mongodb

import com.mongodb.DB
import java.util.Date
import org.dictat.wikistat.model.PageActivity
import org.dictat.wikistat.services.PageDao
import org.dictat.wikistat.model.TimeFrame
import com.mongodb.BasicDBObject
import java.util.ArrayList
import com.mongodb.DBObject
import org.dictat.wikistat.utils.LazyIterable
import org.slf4j.LoggerFactory
import java.util.HashMap
import java.util.Collections
import com.mongodb.WriteConcern
import com.mongodb.MapReduceCommand
import java.io.InputStreamReader
import java.io.InputStream
import org.dictat.wikistat.utils.IterableAdapter

public class MongoPageDaoImpl (db: DB, val languages: List<String>): AbstractMongoDao(db), PageDao {
    override fun findExtraordinaryActivity(lang: String, date: Date): Iterable<String> {

        val mapCommand = getScriptResouce("extra_activity.map.js");
        val reduceCommand = getScriptResouce("extra_activity.reduce.js");
        val command = MapReduceCommand(
                db.getCollection(lang)!!,
                mapCommand,
                reduceCommand,
                null,
                MapReduceCommand.OutputType.INLINE,
                BasicDBObject());

        return IterableAdapter(db.getCollection(lang)!!.mapReduce(command)!!.results()!!, {it.get("") as String})
    }

    fun getScriptResouce(resourceFile: String): String {
        val f: InputStream = Thread.currentThread().getContextClassLoader()?.getResourceAsStream("org/dictat/wikistat/services/mongodb/" + resourceFile)!!;
        return InputStreamReader(f).readText()
    }
    override fun getCollectionNames(): List<String> {
        return languages
    }

    override fun getIndices(): List<String> {
        return Collections.emptyList()
    }

    object constants {
        val logger = LoggerFactory.getLogger(javaClass<MongoPageDaoImpl>())!!
    }

    override fun removePage(page: String, lang: String) {
        db.getCollection(lang)!!.remove(BasicDBObject("_id", page))
    }
    override fun getPageNames(lang: String): LazyIterable<String> {
        return Cursor(db.getCollection(lang)!!.find(BasicDBObject(), BasicDBObject("_id", 1))!!, { it.get("_id") as String })
    }
    override fun removePageEvents(name: String, lang: String, times: List<Pair<Date, TimeFrame>>) {
        throw UnsupportedOperationException()
    }
    override fun getLangs(): List<String> {
        return Collections.unmodifiableList(languages)
    }
    override fun findPages(prefix: String, lang: String, max: Int): List<String> {
        val query = BasicDBObject("_id", BasicDBObject("\$regex", "^" + prefix + ".*")).append("\$where", "this.c != null");
        val pages = db.getCollection(lang)!!.find(query, BasicDBObject("_id", 1))!!.sort(BasicDBObject("_id", 1))!!.limit(50);
        val ret = ArrayList<String>()
        for (page in pages) {
            ret.add(page.get("_id") as String);
        }
        return ret
    }
    override fun getPageActivity(name: String, lang: String): List<PageActivity> {
        val obj = db.getCollection(lang)!!.findOne(BasicDBObject("_id", name))
        if(obj?.get("h") == null) {
            return Collections.emptyList();
        }
        val dbActivity = obj?.get("h") as DBObject

        val ret = HashMap<String, PageActivity>()
        for(key in dbActivity.keySet()?.iterator()) {
            val activity = PageActivity()
            activity.time = DateUtil.stringToDate(key, TimeFrame.Hour)
            activity.activity = dbActivity.get(key) as Int
            ret.put(key, activity)
        }
        val r = ArrayList<PageActivity>()
        ret.values().forEach { r.add(it) }
        return r;
    }
    override fun savePageEvent(
            name: String,
            lang: String,
            cnt: Int,
            time: Date,
            timeFrame: TimeFrame,
            special: Boolean) {
        val key = getKey(timeFrame, special)
        db.getCollection(lang)!!.update(
                BasicDBObject("_id", name),
                BasicDBObject("\$inc", BasicDBObject(key + "." + DateUtil.dateToString(time, timeFrame), cnt)),
                true,
                false,
                WriteConcern.JOURNALED)
    }

    fun getKey(timeFrame: TimeFrame, special: Boolean): String {
        return timeFrame.symbol.toString() + (if(special) "s" else "");
    }

    override fun markChecked(page: String, lang: String) {
        db.getCollection(lang)!!.update(BasicDBObject("_id", page), BasicDBObject("\$set", BasicDBObject("c", DateUtil.dateToString(Date(), TimeFrame.Hour))))
    }

    override fun findUnchecked(lang: String): LazyIterable<String> {
        val projection = BasicDBObject("_id", 1)
        val filter = BasicDBObject("\$where", "this.c == null")
        return Cursor(db.getCollection(lang)!!.find(filter, projection)!!, { it.get("_id") as String })
    }

    override fun replaceSum(
            page: String,
            lang: String,
            from: TimeFrame,
            to: TimeFrame,
            fromTime: Date,
            toTime: Date,
            sum: Int) {
        //this is quite ineffective in mongodb, we have to send the set and the unset commands
        val updateDoc =
                BasicDBObject(
                        "\$set",
                        BasicDBObject(getKey(to, false) + "." + DateUtil.dateToString(fromTime, to), sum))


        val key = BasicDBObject("_id", page)

        val unsets = BasicDBObject()
        for(i in DateUtil.datesBetween(fromTime, toTime, from)) {
            unsets.append(getKey(from, false) + "." + i, 0)
        }
        updateDoc.append("\$unset", unsets)
        db.getCollection(lang)!!.update(key, updateDoc, true, false, WriteConcern.JOURNALED)

    }

}
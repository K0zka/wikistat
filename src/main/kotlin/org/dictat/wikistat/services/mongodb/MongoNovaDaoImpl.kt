package org.dictat.wikistat.services.mongodb

import com.mongodb.DB
import org.dictat.wikistat.services.NovaDao
import org.dictat.wikistat.model.Nova
import com.mongodb.DBCollection

public class MongoNovaDaoImpl(db: DB) : AbstractMongoDao(db), NovaDao {
    fun getCollection() : DBCollection {
        return db.getCollection("novas")!!
    }
    override fun getNovasFor(page: String, lang: String) {
        getCollection().find()
    }
    override fun regEvent(nova: Nova) {

    }
    override fun getCollectionNames(): List<String> {
        return listOf("novas")
    }
    override fun getIndices(): List<String> {
        return listOf("p", "t", "s")
    }
}

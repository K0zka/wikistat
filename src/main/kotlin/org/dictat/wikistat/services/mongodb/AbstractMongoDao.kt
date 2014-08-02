package org.dictat.wikistat.services.mongodb

import com.mongodb.DB
import org.slf4j.LoggerFactory
import com.mongodb.BasicDBObject

/**
 * Abstract baseclass for MongoDB-based DAO classes.
 */
abstract class AbstractMongoDao(val db: DB) {
    private object constants {
        val logger = LoggerFactory.getLogger( javaClass<AbstractMongoDao>() )
    }
    abstract fun getCollectionNames() : List<String>
    abstract fun getIndices() : List<String>
    fun initialize() {
        for(collection in getCollectionNames()) {
            if(db.getCollection(collection) == null) {
                db.createCollection(collection, BasicDBObject())
            }
            for(index in getIndices()) {
                db.getCollection(collection)?.ensureIndex(BasicDBObject(index, 1))
            }
        }
    }
}
package org.dictat.wikistat.services.mongodb

import com.mongodb.DB
import org.slf4j.LoggerFactory
import com.mongodb.DBObject
import com.mongodb.BasicDBObject
import org.apache.commons.io.IOUtils
import org.bson.types.Code

object SchemaUtils {
    fun readFromResource(resource : String) : String {
        var ret : String? = null
        Thread.currentThread().getContextClassLoader()!!.getResourceAsStream("org/dictat/wikistat/services/mongodb/" + resource + ".js")?.use {
            ret = IOUtils.toString(it)
        }
        return ret!!
    }
}

public class MongoSchemaInitializer(val db: DB, val scripts : List<String>) {
    object constants {
        val logger = LoggerFactory.getLogger(javaClass<MongoSchemaInitializer>())!!
    }

    fun init() : Unit {
        val systemJs = db.getCollection("system.js") //there must be a system.js collection
        for(scriptName in scripts) {
            constants.logger.info("Checking {}", scriptName)
            val function = systemJs?.findOne(BasicDBObject("_id", scriptName))
            val script = SchemaUtils.readFromResource(scriptName)
            val scriptCode = Code(script)
            if(function == null || !scriptCode.equals(function.get("value"))) {
                constants.logger.info("Installing {}", scriptName)
                systemJs?.save(BasicDBObject("_id", scriptName).append("value", scriptCode))
            }
        }
    }
}
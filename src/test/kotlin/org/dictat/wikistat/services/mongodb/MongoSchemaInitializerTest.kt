package org.dictat.wikistat.services.mongodb

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import org.mockito.Mock
import com.mongodb.DB
import org.mockito.Mockito
import com.mongodb.DBCollection
import com.mongodb.DBObject
import org.mockito.Matchers
import org.mockito.stubbing.Answer
import org.mockito.invocation.InvocationOnMock
import com.mongodb.BasicDBObject

RunWith(javaClass<MockitoJUnitRunner>())
public class MongoSchemaInitializerTest {
    Mock var db: DB? = null
    Mock var systemJsCollection: DBCollection? = null

    Test fun readFromResource() {
        SchemaUtils.readFromResource("strtodate")
    }
    Test fun init() {
        val initializer = MongoSchemaInitializer(db!!, listOf("strtodate", "datetostr"))
        Mockito.`when`(db?.getCollection("system.js"))!!.thenReturn(systemJsCollection)
        initializer.init()
        Mockito.verify(db)!!.getCollection("system.js")
        Mockito.verify(systemJsCollection, Mockito.times(2))!!.save(Matchers.any(javaClass<DBObject>()))
    }
    Test fun initWithExistingOutdated() {
        val initializer = MongoSchemaInitializer(db!!, listOf("strtodate", "datetostr"))
        Mockito.`when`(db?.getCollection("system.js"))!!.thenReturn(systemJsCollection)
        Mockito.`when`(systemJsCollection?.findOne(Matchers.any(javaClass<DBObject>())))?.thenAnswer(object : Answer<DBObject> {
            override fun answer(invocation: InvocationOnMock?): DBObject? {
                val query = invocation?.getArguments()!![0] as DBObject
                return BasicDBObject("_id", query.get("_id")).append("value", "outdated function body")
            }
        })
        initializer.init()
        Mockito.verify(db)!!.getCollection("system.js")
        Mockito.verify(systemJsCollection, Mockito.times(2))!!.save(Matchers.any(javaClass<DBObject>()))
    }

    Test fun initWithExistingUptodate() {

        val initializer = MongoSchemaInitializer(db!!, listOf("strtodate", "datetostr"))
        Mockito.`when`(db?.getCollection("system.js"))!!.thenReturn(systemJsCollection)
        Mockito.`when`(systemJsCollection?.findOne(Matchers.any(javaClass<DBObject>())))?.thenAnswer(object : Answer<DBObject> {
            override fun answer(invocation: InvocationOnMock?): DBObject? {
                val query = invocation?.getArguments()!![0] as DBObject
                val id = query.get("_id") as String
                return BasicDBObject("_id", id).append("value", SchemaUtils.readFromResource(id))
            }
        })
        initializer.init()
        Mockito.verify(db)!!.getCollection("system.js")
        Mockito.verify(systemJsCollection, Mockito.never())!!.save(Matchers.any(javaClass<DBObject>()))
    }

}
package org.dictat.wikistat.services.mongodb

import com.mongodb.DB
import org.junit.Before
import org.mockito.Mockito
import org.junit.Test
import java.util.Date
import org.dictat.wikistat.model.TimeFrame
import com.mongodb.DBCollection
import com.mongodb.DBObject
import org.mockito.Matchers
import com.mongodb.WriteConcern
import com.mongodb.BasicDBObject
import com.mongodb.DBCursor
import org.mockito.internal.creation.AbstractMockitoMethodProxy
import java.text.SimpleDateFormat

public class MongoPageDaoImplTest {
    var db: DB? = null
    var dao: MongoPageDaoImpl? = null
    Before fun setupDB() {
        db = Mockito.mock(javaClass<DB>())
        dao = MongoPageDaoImpl(db!!, listOf("hu", "sk", "jp"))
    }
    Test fun testSavePageEvent() {
        val collection = Mockito.mock(javaClass<DBCollection>())
        Mockito.`when`(db!!.getCollection("en"))!!.`thenReturn`(collection)
        dao!!.savePageEvent("Jeno Kakukk", "en", 1, Date(), TimeFrame.Hour, false)
        Mockito.verify(collection)!!.update(
                Matchers.`any`(javaClass<DBObject>()),
                Matchers.`any`(javaClass<DBObject>()),
                Matchers.`any`(javaClass<Boolean>())!!,
                Matchers.`any`(javaClass<Boolean>())!!,
                Matchers.`any`(javaClass<WriteConcern>()))
    }
    Test fun testFindPages() {
        val collection = Mockito.mock(javaClass<DBCollection>())
        val cursor = Mockito.mock(javaClass<DBCursor>())
        Mockito.`when`(cursor?.sort( Matchers.any( javaClass<BasicDBObject>() ) ))!!.thenReturn(cursor)
        Mockito.`when`(cursor?.limit( Matchers.anyInt() ))!!.thenReturn(cursor)
        Mockito.`when`(collection!!.find(
                Matchers.any(javaClass<BasicDBObject>()),
                Matchers.any(javaClass<BasicDBObject>())))!!
                .thenReturn(cursor)
        Mockito.`when`(db!!.getCollection("en"))!!.`thenReturn`(collection)
        dao!!.findPages("Jeno ", "en", 20)
        Mockito.verify(collection)!!.find(
                Matchers.any(javaClass<BasicDBObject>()),
                Matchers.any(javaClass<BasicDBObject>()))
    }
    Test fun replaceSum() {
        val collection = Mockito.mock(javaClass<DBCollection>())
        Mockito.`when`(db!!.getCollection("en"))!!.`thenReturn`(collection)
        val sdf = SimpleDateFormat("yyyy-mm-dd")
        dao!!.replaceSum(
                "Jeno Kakukk",
                "en",
                TimeFrame.Hour,
                TimeFrame.Day,
                sdf.parse("2010-01-01")!!,
                sdf.parse("2010-01-02")!!,
                1000)
    }
}
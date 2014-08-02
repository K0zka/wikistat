package org.dictat.wikistat.services.mongodb

import org.springframework.beans.factory.FactoryBean
import com.mongodb.DB
import com.mongodb.Mongo

public class MongoDbFactory (val addr : String, val dbName : String) : FactoryBean<DB>{
    public override fun getObject(): DB? {
        return Mongo(addr).getDB(dbName);
    }
    public override fun getObjectType(): Class<out Any?>? {
        return javaClass<DB>()
    }
    public override fun isSingleton(): Boolean {
        return false;
    }

}
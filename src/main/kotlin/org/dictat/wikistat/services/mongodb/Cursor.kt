package org.dictat.wikistat.services.mongodb

import com.mongodb.DBCursor
import org.dictat.wikistat.utils.LazyIterable
import com.mongodb.DBObject

/**
 * A LazyIterable object that uses a clojure to map the DBObject to a java object.
 */
public class Cursor<T>(val cursor : DBCursor, val mapping : (DBObject) -> T ) : LazyIterable<T> {
    public override fun close() {
        cursor.close()
    }
    public override fun iterator(): Iterator<T> {
        return CursorIterator<T>(cursor, mapping);
    }
}
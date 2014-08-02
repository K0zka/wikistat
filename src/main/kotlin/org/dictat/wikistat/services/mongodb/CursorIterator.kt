package org.dictat.wikistat.services.mongodb

import com.mongodb.DBObject
import com.mongodb.DBCursor

public class CursorIterator<T>(val cursor : DBCursor, val mapping : (DBObject) -> T) : Iterator<T> {
    public override fun next(): T {
        return mapping(cursor.next())
    }
    public override fun hasNext(): Boolean {
        return cursor.hasNext();
    }
}
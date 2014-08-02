package org.dictat.wikistat.utils

public class IteratorAdapter<R, T>(val iterator : Iterator<R>, val mapping : (R) -> T) : Iterator<T> {
    public override fun next(): T {
        return mapping(iterator.next())
    }
    public override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

}
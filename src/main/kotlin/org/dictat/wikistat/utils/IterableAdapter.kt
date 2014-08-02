package org.dictat.wikistat.utils


public class IterableAdapter<R, T>(val iterable : MutableIterable<R>, val mapping : (R) -> T) : Iterable<T> {
    public override fun iterator(): Iterator<T> {
        return IteratorAdapter(iterable.iterator(), mapping)
    }

}
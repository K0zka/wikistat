package org.dictat.wikistat.utils

import java.io.Closeable

/**
 * This interface is for iterable objects, that must be closed after use. E.g. database result sets.
 */
public trait LazyIterable<T> : Iterable<T> , Closeable {

}
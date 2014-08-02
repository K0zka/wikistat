package org.dictat.wikistat.utils

public inline fun <T>Comparable<T>.isEqualWith(other : T):Boolean = compareTo(other) == 0


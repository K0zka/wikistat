package org.dictat.wikistat.utils

import java.util.concurrent.Future
import java.util.concurrent.ExecutorService
import java.util.concurrent.Callable

public class PrioritizedCallable<T> (val action: ()->T, val priority : Int) : Callable<T>, Comparable<PrioritizedCallable<T>> {
    public override fun call(): T {
        return action()
    }
    public override fun compareTo(other: PrioritizedCallable<T>): Int {
        return this.priority - other.priority
    }
}

public inline fun <T>ExecutorService.submit(action: ()->T, priority : Int):Future<T> {
    return submit(PrioritizedCallable(action, priority))
}

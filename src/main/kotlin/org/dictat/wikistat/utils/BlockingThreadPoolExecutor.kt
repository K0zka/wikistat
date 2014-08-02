package org.dictat.wikistat.utils

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.BlockingQueue
import java.util.concurrent.RunnableFuture
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

public class StrictBlockingQueue<T>(val queue: BlockingQueue<T>): BlockingQueue<T> {
    public override fun add(e: T): Boolean {
        return queue.add(e)
    }
    public override fun offer(e: T): Boolean {
        //this is the trick, all the rest is delegate code
        put(e)
        return true
    }
    public override fun put(e: T) {
        queue.put(e)
    }
    public override fun offer(e: T, timeout: Long, unit: TimeUnit): Boolean {
        put(e)
        return true
    }
    public override fun take(): T? {
        return queue.take()
    }
    public override fun poll(timeout: Long, unit: TimeUnit): T {
        return queue.poll(timeout, unit)
    }
    public override fun remainingCapacity(): Int {
        return queue.remainingCapacity()
    }
    public override fun remove(o: Any?): Boolean {
        return queue.remove(o)
    }
    public override fun contains(o: Any?): Boolean {
        return queue.contains(o)
    }
    public override fun drainTo(c: MutableCollection<in T>): Int {
        return queue.drainTo(c)
    }
    public override fun drainTo(c: MutableCollection<in T>, maxElements: Int): Int {
        return queue.drainTo(c, maxElements)
    }
    public override fun remove(): T {
        return queue.remove()
    }
    public override fun poll(): T? {
        return queue.poll()
    }
    public override fun element(): T {
        return queue.element()
    }
    public override fun peek(): T? {
        return queue.peek()
    }
    public override fun size(): Int {
        return queue.size()
    }
    public override fun isEmpty(): Boolean {
        return queue.isEmpty()
    }
    public override fun containsAll(c: Collection<Any?>): Boolean {
        return queue.containsAll(c)
    }
    public override fun addAll(c: Collection<T>): Boolean {
        return queue.addAll(c)
    }
    public override fun removeAll(c: Collection<Any?>): Boolean {
        return queue.removeAll(c)
    }
    public override fun retainAll(c: Collection<Any?>): Boolean {
        return queue.retainAll(c)
    }
    public override fun clear() {
        return queue.clear()
    }
    public override fun hashCode(): Int {
        return queue.hashCode()
    }
    public override fun equals(other: Any?): Boolean {
        return queue.equals(other)
    }
    public override fun iterator(): MutableIterator<T> {
        return queue.iterator()
    }
}

class NamedThreadFactory(val namePrefix: String): ThreadFactory {
    var cntr = AtomicInteger()
    public override fun newThread(r: Runnable): Thread {
        val ret = Thread(r, namePrefix + cntr.getAndIncrement())
        return ret;
    }

}


public class BlockingThreadPoolExecutor(corePoolSize: Int,
                                        maxPoolSize: Int,
                                        keepAlive: Long,
                                        unit: TimeUnit,
                                        name: String,
                                        val queue: BlockingQueue<Runnable>)
: ThreadPoolExecutor(
        corePoolSize,
        maxPoolSize,
        keepAlive,
        unit,
        StrictBlockingQueue(queue),
        NamedThreadFactory(name)) {

    public class ComparableRunnableFuture<T> (val callable: Callable<T>): FutureTask<T>(callable), Comparable<ComparableRunnableFuture<T>> {
        public override fun compareTo(other: ComparableRunnableFuture<T>): Int {
            return compare(callable as Comparable<Any>, other.callable as Comparable<Any>);
        }
        fun compare (first: Comparable<Any>, second: Comparable<Any>): Int {
            return  first.compareTo(second)
        }
    }

    protected fun <T> newTaskFor(callable: Callable<T>): RunnableFuture<T>? {
        return ComparableRunnableFuture(callable)
    }


}
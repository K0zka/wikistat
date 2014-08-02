package org.dictat.wikistat.utils

import org.junit.Test
import java.util.concurrent.PriorityBlockingQueue
import org.junit.Assert

public class PrioritizedCallableTest {
    Test fun sendThroughPriorityQueue() {
        val queue = PriorityBlockingQueue<PrioritizedCallable<String>>()
        queue.put(PrioritizedCallable<String>({ "A" }, 4))
        queue.put(PrioritizedCallable<String>({ "B" }, 1))
        queue.put(PrioritizedCallable<String>({ "C" }, 2))
        queue.put(PrioritizedCallable<String>({ "D" }, 3))

        Assert.assertEquals("B", queue.remove().call());
        Assert.assertEquals("C", queue.remove().call());
        Assert.assertEquals("D", queue.remove().call());
        Assert.assertEquals("A", queue.remove().call());
    }

    Test fun compare() {
        //comparison based on equal priority
        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 1).isEqualWith (PrioritizedCallable<String>({ "B" }, 1)))
        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 0).isEqualWith (PrioritizedCallable<String>({ "B" }, 0)))

        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 0) < (PrioritizedCallable<String>({ "B" }, 1)))
        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 0) <= (PrioritizedCallable<String>({ "B" }, 1)))
        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 0) <= (PrioritizedCallable<String>({ "B" }, 0)))

        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 1) > (PrioritizedCallable<String>({ "B" }, 0)))
        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 1) >= (PrioritizedCallable<String>({ "B" }, 0)))
        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 1) >= (PrioritizedCallable<String>({ "B" }, 0)))

        Assert.assertTrue(PrioritizedCallable<String>({ "A" }, 0).isEqualWith (PrioritizedCallable<String>({ "A" }, 0)))
    }
}


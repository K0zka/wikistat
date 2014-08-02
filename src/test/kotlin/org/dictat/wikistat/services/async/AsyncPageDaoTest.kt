package org.dictat.wikistat.services.async

import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.ExecutorService
import org.dictat.wikistat.model.TimeFrame
import org.dictat.wikistat.services.PageDao

public class AsyncPageDaoTest {
    Test fun save() {
        val mock = Mockito.mock(javaClass<PageDao>())!!
        val executor = Mockito.mock(javaClass<ExecutorService>())!!
        val dao = AsyncPageDao(mock, executor);
        dao.savePageEvent("bla", "bla", 100, java.util.Date(), TimeFrame.Hour, false);
    }
    Test fun stressTestWithMocks() {
        val mock = Mockito.mock(javaClass<PageDao>())!!
        val executor = Mockito.mock(javaClass<ExecutorService>())!!
        val dao = AsyncPageDao(mock, executor);
        for (i in 1..100000) {
            dao.savePageEvent("bla", "bla", 100, java.util.Date(), TimeFrame.Hour, false);
        }
    }
}
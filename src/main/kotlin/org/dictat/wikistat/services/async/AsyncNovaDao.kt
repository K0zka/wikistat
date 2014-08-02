package org.dictat.wikistat.services.async

import java.util.concurrent.ExecutorService
import org.dictat.wikistat.model.Nova
import org.dictat.wikistat.utils.submit
import org.dictat.wikistat.services.NovaDao

public class AsyncNovaDao(val novaDao: NovaDao, val executor: ExecutorService): NovaDao {
    override fun regEvent(nova: Nova) {
        executor.submit({ novaDao.regEvent(nova) }, Priority.updatePriority)
    }
    override fun getNovasFor(page: String, lang: String) {
        executor.submit({ novaDao.getNovasFor(page, lang) }, Priority.readPriority)
    }
}
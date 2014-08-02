package org.dictat.wikistat.services

import org.dictat.wikistat.model.Nova

public trait NovaDao {
    fun regEvent(nova: Nova);
    fun getNovasFor(page : String, lang: String)
}
package org.dictat.wikistat.servlets

import org.dictat.wikistat.services.PageDao

abstract class PageDaoServlet<T> : SpringJsonServlet<T, PageDao>() {
    override fun getBeanName(): String {
        return "pageDao"
    }
}
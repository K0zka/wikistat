package org.dictat.wikistat.servlets

import org.dictat.wikistat.model.PageActivity
import org.dictat.wikistat.services.StatDao
import javax.servlet.http.HttpServletRequest
import org.apache.commons.lang.StringUtils

class StatsServlet(): SpringJsonServlet<List<PageActivity>, StatDao>() {
    override fun getData(req: HttpServletRequest): List<PageActivity> {
        return getBean().getStatistics(StringUtils.substringAfterLast(req.getRequestURI(), "/")!!)
    }
    override fun getBeanName(): String =
            "statDao"

}
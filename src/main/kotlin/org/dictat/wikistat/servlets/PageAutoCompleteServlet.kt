package org.dictat.wikistat.servlets

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.dictat.wikistat.services.PageDao
import org.springframework.web.context.support.WebApplicationContextUtils
import com.fasterxml.jackson.databind.ObjectMapper

public final class PageAutoCompleteServlet : PageDaoServlet<List<String>>() {
    override fun getData(req: HttpServletRequest): List<String> {
        return getBean().findPages(req.getParameter("term")!!, req.getParameter("lang")!!, 20);
    }
}
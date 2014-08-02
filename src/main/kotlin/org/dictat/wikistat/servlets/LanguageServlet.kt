package org.dictat.wikistat.servlets

import javax.servlet.http.HttpServlet
import org.dictat.wikistat.services.PageDao
import org.springframework.web.context.support.WebApplicationContextUtils
import javax.servlet.ServletConfig
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.fasterxml.jackson.databind.ObjectMapper

public final class LanguageServlet : PageDaoServlet<List<String>>() {
    override fun getData(req: HttpServletRequest): List<String> {
        return getBean().getLangs();
    }
}
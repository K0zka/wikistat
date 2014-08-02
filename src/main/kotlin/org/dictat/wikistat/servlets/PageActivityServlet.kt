package org.dictat.wikistat.servlets

import com.fasterxml.jackson.databind.ObjectMapper
import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.dictat.wikistat.services.PageDao
import org.springframework.web.context.support.WebApplicationContextUtils
import java.net.URLDecoder
import org.dictat.wikistat.model.PageActivity

public final class PageActivityServlet: PageDaoServlet<List<PageActivity>>() {
    override fun getData(req: HttpServletRequest): List<PageActivity> {
        val uri = req.getRequestURI()?.split("/")!!;
        val lang = uri.get(uri.size - 2);
        val page = URLDecoder.decode(uri.get(uri.size - 1), "UTF-8");

        return getBean().getPageActivity(page, lang)
    }
}
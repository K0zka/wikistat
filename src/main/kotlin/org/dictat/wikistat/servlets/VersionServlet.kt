package org.dictat.wikistat.servlets

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.dictat.wikistat.utils.VersionUtils

public class VersionServlet : JsonServlet<String>() {
    override fun getData(req: HttpServletRequest): String {
        return VersionUtils.getVersion();
    }
}
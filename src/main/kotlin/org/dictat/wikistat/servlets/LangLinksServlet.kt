package org.dictat.wikistat.servlets

import org.dictat.wikistat.wikidata.WikiData
import javax.servlet.http.HttpServletRequest
import java.net.URLDecoder

public class LangLinksServlet : SpringJsonServlet<Map<String, String>, WikiData>() {
    override fun getBeanName(): String {
        return "wikiData"
    }
    override fun getData(req: HttpServletRequest): Map<String, String> {
        val uri = req.getRequestURI()?.split("/")!!;
        val lang = uri.get(uri.size - 2);
        val page = URLDecoder.decode(uri.get(uri.size - 1), "UTF-8");

        return getBean().langLinks(page, lang);
    }
}
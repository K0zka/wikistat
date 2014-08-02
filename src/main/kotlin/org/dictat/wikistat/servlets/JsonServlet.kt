package org.dictat.wikistat.servlets

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory

abstract class JsonServlet<T> : HttpServlet() {
    private object constants {
        val logger = LoggerFactory.getLogger(javaClass<JsonServlet<*>>())!!
    }
    abstract fun getData(req: HttpServletRequest): T;
    protected override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val mapper = ObjectMapper()
        resp?.setContentType("application/json");
        resp!!.setCharacterEncoding("UTF-8");
        try {
            mapper.writeValue(resp.getOutputStream(), getData(req!!));
        } catch (e : Exception) {
            constants.logger.error("Could not get the data", e)
        }
    }
}
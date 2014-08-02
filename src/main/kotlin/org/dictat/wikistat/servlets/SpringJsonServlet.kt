package org.dictat.wikistat.servlets

import org.springframework.web.context.support.WebApplicationContextUtils

abstract class SpringJsonServlet<T, DAO>: JsonServlet<T>(){
    abstract fun getBeanName(): String;
    fun getBean(): DAO {
        return  WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext())?.getBean(getBeanName()) as DAO
    }
}
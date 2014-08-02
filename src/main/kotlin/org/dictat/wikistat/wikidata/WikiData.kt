package org.dictat.wikistat.wikidata

import java.util.HashMap
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.AutoRetryHttpClient
import org.apache.http.impl.client.DecompressingHttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import java.net.URLEncoder
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.Collections
import org.apache.http.protocol.HTTP
import org.dictat.wikistat.utils.VersionUtils

class WikiData() {

    class PageMissingException() : Exception() {

    }

    fun langLinks(wikipage: String, lang: String): Map<String, String> {
        //TODO: this method is messy, cleanup needed
        val ret = HashMap<String, String>();
        val defaultHttpClient = DefaultHttpClient()
        defaultHttpClient.getParams()!!.setParameter(HTTP.USER_AGENT,"wikistat robot "+  VersionUtils.getVersion() + " http://dictat.org/");
        val client: HttpClient = AutoRetryHttpClient(DecompressingHttpClient(defaultHttpClient))
        val req = HttpGet("https://"+lang+".wikipedia.org/w/api.php?format=json&action=query&prop=langlinks&lllimit=500&titles="+URLEncoder.encode(wikipage,"UTF-8"));
        val response = client.execute(req);
        val json = ObjectMapper().readTree(response!!.getEntity()!!.getContent());
        val page = json?.get("query")?.get("pages")
        if(page == null) {
            throw PageMissingException()
        }
        for(elem in page.elements()) {
            val langlinks = elem.get("langlinks")
            if(langlinks != null) {
                for(sp in langlinks.elements()) {
                    ret.put(sp.get("lang")?.textValue()!!, sp.get("*")?.textValue()!!);
                }
            } else {
                if(elem.get("missing") != null) {
                    throw PageMissingException()
                }
            }
        }
        return ret;
    }
}
package org.dictat.wikistat.utils

import java.net.URLDecoder

public object StringUtils {
    fun decodeString(encoded: String) : String {
        return URLDecoder.decode(URLDecoder.decode(encoded.replaceAll("\\\\x","%"), "UTF-8"), "UTF-8")
                .replaceAll("_", " ").trim()
    }
    fun removeSlashes(str:String) : String {
        if(str.startsWith('/')) {
            return removeSlashes(str.substring(1))
        } else {
            return str.trim()
        }
    }
}
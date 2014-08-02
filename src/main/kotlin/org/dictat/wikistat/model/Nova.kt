package org.dictat.wikistat.model

import java.util.Date
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A nova is an exceptional (over-average) behavior of a page.
 */
public data class Nova {
    JsonProperty("s")
    var start: Date? = null
    JsonProperty("e")
    var end: Date? = null
    JsonProperty("n")
    var novaType: NovaType? = null
    JsonProperty("l")
    var lang: String? = null
    JsonProperty("p")
    var page: String? = null
}
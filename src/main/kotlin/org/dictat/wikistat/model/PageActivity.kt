package org.dictat.wikistat.model

import java.util.Date
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Activity of a page in a given time
 */
public class PageActivity {
    JsonProperty("t")
    var time : Date? = null
    JsonProperty("a")
    var activity : Int? = null
}
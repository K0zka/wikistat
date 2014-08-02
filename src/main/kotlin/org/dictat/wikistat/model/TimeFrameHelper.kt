package org.dictat.wikistat.model

public object TimeFrameHelper {
    fun getByChar(chr: Char): TimeFrame? {
        for (timeFrame in TimeFrame.values()) {
            if (chr == timeFrame.symbol)
                return timeFrame
        }
        throw IllegalArgumentException("")
    }
}
package org.dictat.wikistat.model

public enum class TimeFrame(val symbol: Char) {
    Hour : TimeFrame('h')
    Day : TimeFrame('d')
    Week : TimeFrame('w')
    Month : TimeFrame('m')
}
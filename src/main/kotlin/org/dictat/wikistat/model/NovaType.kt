package org.dictat.wikistat.model

/**
 * Description of classes of exceptional load on wikipedia articles.
 */
enum class NovaType {
    /**
     * The page is requested more than 100 times of its average and over the 0.1 percent of the total wiki site traffic.
     */
    Supernova
    /**
     * The page is requested more than 10 times of its average.
     */
    Burst
    /**
     * The page is requested more than 5 times of its average, the average must be over 40 hits/hour.
     */
    Flash
}
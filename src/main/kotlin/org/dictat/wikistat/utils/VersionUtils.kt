package org.dictat.wikistat.utils

public object VersionUtils  {
    val versionStr: String = "\$HeadURL: https://dictat.org/svn/etc/wikistat/trunk/src/main/kotlin/org/dictat/wikistat/utils/VersionUtils.kt $";
    fun getVersion(): String {
        return versionStr.substring(versionStr.indexOf("wikistat/") + 9, versionStr.indexOf("/src/"))
                .replaceAll("tags", "")
                .replaceAll("/", "");
    }
}
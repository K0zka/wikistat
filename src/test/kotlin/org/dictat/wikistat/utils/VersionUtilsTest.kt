package org.dictat.wikistat.utils

import org.junit.Test
import org.junit.Assert

public class VersionUtilsTest {
    [Test] fun testGetVersion() {
        Assert.assertNotNull(VersionUtils.getVersion())
    }
}
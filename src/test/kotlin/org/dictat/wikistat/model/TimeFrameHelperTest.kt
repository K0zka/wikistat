package org.dictat.wikistat.model

import org.junit.Test
import org.junit.Assert

class TimeFrameHelperTest {
    Test fun getByChar() {
        for(tf in TimeFrame.values()) {
            Assert.assertEquals(tf, TimeFrameHelper.getByChar(tf.symbol))
        }
    }
    Test(expected=javaClass<IllegalArgumentException>()) fun getByCharWithUnknownChar() {
        TimeFrameHelper.getByChar('?')
    }
}
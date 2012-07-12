package com.taobao.tdhs.jdbc.sqlparser;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-7-12 上午11:28
 */
public class HintStructTest {
    @Test
    public void testAnalyzeHint1() throws Exception {
        HintStruct h = new HintStruct("/*tdhs:<213>[ind(a,b)]*/");
        h.AnalyzeHint();
        assertEquals(213, h.getHash());
        assertEquals("ind", h.getIndexName());
        assertArrayEquals(new String[]{"a", "b"}, h.getListIndexColumns().toArray(new String[2]));

    }

    @Test
    public void testAnalyzeHint2() throws Exception {
        HintStruct h = new HintStruct("/*tdhs:<21ad3>[ind(a,b)]*/");
        h.AnalyzeHint();
        assertEquals(0, h.getHash());
        assertEquals("ind", h.getIndexName());
        assertArrayEquals(new String[]{"a", "b"}, h.getListIndexColumns().toArray(new String[2]));

    }

    @Test
    public void testAnalyzeHint3() throws Exception {
        HintStruct h = new HintStruct("/*tdhs:[ind(a,b)]*/");
        h.AnalyzeHint();
        assertEquals(0, h.getHash());
        assertEquals("ind", h.getIndexName());
        assertArrayEquals(new String[]{"a", "b"}, h.getListIndexColumns().toArray(new String[2]));

    }

    @Test
    public void testAnalyzeHint4() throws Exception {
        HintStruct h = new HintStruct("/*tdhs:<123>*/");
        h.AnalyzeHint();
        assertEquals(123, h.getHash());
        assertEquals("", h.getIndexName());
        assertEquals(0, h.getListIndexColumns().size());

    }

    @Test
    public void testAnalyzeHint5() throws Exception {
        HintStruct h = new HintStruct("/*tdhs:<123>asdfasdfasdf*/");
        h.AnalyzeHint();
        assertTrue(StringUtils.isNotBlank(h.getErrmsg()));

    }
}

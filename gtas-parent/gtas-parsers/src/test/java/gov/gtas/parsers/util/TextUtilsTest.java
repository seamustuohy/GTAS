package gov.gtas.parsers.util;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Test;

public class TextUtilsTest {
    @Test
    public void testSplit() {
        String[] strs = TextUtils.splitWithEscapeChar("mc?'foo'bar", '\'', '?');
        System.out.println(Arrays.toString(strs));
        assertEquals("mc'foo", strs[0]);
        assertEquals("bar", strs[1]);
    }
    
    @Test
    public void testSplitHappyPath() {
        String segmentText = "DTM*36:10109$LOC*31*USA$NAD*FL***ANDREWS:TIFFANY:PAGE$ATT*2**F$";
        String segs[] = TextUtils.splitWithEscapeChar(segmentText, '$', '?');
        assertEquals(4, segs.length);
        assertEquals("DTM*36:10109", segs[0]);
        assertEquals("LOC*31*USA", segs[1]);
        assertEquals("NAD*FL***ANDREWS:TIFFANY:PAGE", segs[2]);
        assertEquals("ATT*2**F", segs[3]);
    }

    @Test
    public void testSplitWithEscaped() {
        String segmentText = "DTM*36:10109'LOC*31*USA'NAD*FL***MC?'ANDREWS:TIFFANY:PAGE'ATT*2**F'";
        String segs[] = TextUtils.splitWithEscapeChar(segmentText, '\'', '?');
        assertEquals(4, segs.length);
        assertEquals("NAD*FL***MC'ANDREWS:TIFFANY:PAGE", segs[2]);
    }
    
    @Test
    public void testEscapedEscaped() {
        String segmentText = "DTM*36:10109'LOC*31*USA'NAD*FL***MC?'ANDREWS?:TIFF??ANY:PAGE'ATT*2**F'";
        String segs[] = TextUtils.splitWithEscapeChar(segmentText, '\'', '?');
        assertEquals(4, segs.length);
        assertEquals("NAD*FL***MC'ANDREWS?:TIFF??ANY:PAGE", segs[2]);
    }

    @Test
    public void testSplitSegmentsWithExtraneousWhitespace() {
        String segmentText = "DTM*36:10109  $   LOC*31*USA  $NAD*FL***ANDREWS:TIFFANY:PAGE\r\n $\n\n\n\n ATT*2**F$";
        String segs[] = TextUtils.splitWithEscapeChar(segmentText, '$', '?');
        assertEquals(4, segs.length, 4);
        assertEquals("DTM*36:10109", segs[0]);
        assertEquals("LOC*31*USA", segs[1]);
        assertEquals("NAD*FL***ANDREWS:TIFFANY:PAGE", segs[2]);
        assertEquals("ATT*2**F", segs[3]);
    }

    @Test
    public void testSplitElementsWithExtraneousWhitespace() {
        String segmentText = " ANDREWS:    TIFFANY : PAGE ";
        String elements[] = TextUtils.splitWithEscapeChar(segmentText, ':', '?');
        assertEquals("ANDREWS", elements[0]);
        assertEquals("TIFFANY", elements[1]);
        assertEquals("PAGE", elements[2]);
    }
        
    @Test
    public void testMd5() {
        String str1 = "gtas";
        String expected = "2F005A6B1EA39FAA5D75C6CCCF59E63A";
        String actual = TextUtils.getMd5Hash(str1, StandardCharsets.US_ASCII);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConvertToSingleLine() {
        String input = "   hello    \r\n\r\n  there\r   gtas team\n   ";
        String actual = TextUtils.convertToSingleLine(input);
        assertEquals("hellotheregtas team", actual);
    }
    
    @Test
    public void testConvertToSingleLineWhitespace() {
        assertEquals("", TextUtils.convertToSingleLine("      "));
    }
    
    @Test
    public void testIndexOfRegex() {
        String input = "aaaABCbbb";
        String regex = "[A-Z]+";
        int i = TextUtils.indexOfRegex(regex, input);
        assertEquals(input.indexOf('A'), i);
    }
}

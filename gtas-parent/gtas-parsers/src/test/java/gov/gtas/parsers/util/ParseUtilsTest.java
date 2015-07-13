package gov.gtas.parsers.util;
import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class ParseUtilsTest {
    @Test
    public void testSplitHappyPath() {
        String segmentText = "DTM*36:10109$LOC*31*USA$NAD*FL***ANDREWS:TIFFANY:PAGE$ATT*2**F$";
        String segs[] = ParseUtils.splitWithEscapeChar(segmentText, '$', '?');
        assertEquals(segs.length, 4);
        assertEquals(segs[0], "DTM*36:10109");
        assertEquals(segs[1], "LOC*31*USA");
        assertEquals(segs[2], "NAD*FL***ANDREWS:TIFFANY:PAGE");
        assertEquals(segs[3], "ATT*2**F");
    }

    @Test
    public void testSplitWithEscaped() {
        String segmentText = "DTM*36:10109'LOC*31*USA'NAD*FL***MC?'ANDREWS:TIFFANY:PAGE'ATT*2**F'";
        String segs[] = ParseUtils.splitWithEscapeChar(segmentText, '\'', '?');
        assertEquals(segs.length, 4);
        assertEquals(segs[2], "NAD*FL***MC'ANDREWS:TIFFANY:PAGE");
    }

    @Test
    public void testSplitSegmentsWithExtraneousWhitespace() {
        String segmentText = "DTM*36:10109  $   LOC*31*USA  $NAD*FL***ANDREWS:TIFFANY:PAGE\r\n $\n\n\n\n ATT*2**F$";
        String segs[] = ParseUtils.splitWithEscapeChar(segmentText, '$', '?');
        assertEquals(segs.length, 4);
        assertEquals(segs[0], "DTM*36:10109");
        assertEquals(segs[1], "LOC*31*USA");
        assertEquals(segs[2], "NAD*FL***ANDREWS:TIFFANY:PAGE");
        assertEquals(segs[3], "ATT*2**F");
    }

    @Test
    public void testSplitElementsWithExtraneousWhitespace() {
        String segmentText = " ANDREWS:    TIFFANY : PAGE ";
        String elements[] = ParseUtils.splitWithEscapeChar(segmentText, ':', '?');
        assertEquals(elements[0], "ANDREWS");
        assertEquals(elements[1], "TIFFANY");
        assertEquals(elements[2], "PAGE");
    }
    
    @Test
    public void testMd5() {
        String str1 = "unoshriram";
        String expected = "95E5F0B6988EC703E832172F70CE7DC7";
        String actual = ParseUtils.getMd5Hash(str1, StandardCharsets.US_ASCII);
        assertEquals(expected, actual);
    }
}

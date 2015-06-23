package gov.gtas.parsers.util;
import static org.junit.Assert.assertEquals;

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
    public void testSplitWithExtraneousWhitespace() {
        String segmentText = "DTM*36:10109  $   LOC*31*USA  $NAD*FL***ANDREWS:TIFFANY:PAGE\r\n $\n\n\n\n ATT*2**F$";
        String segs[] = ParseUtils.splitWithEscapeChar(segmentText, '$', '?');
        assertEquals(segs.length, 4);
        assertEquals(segs[0], "DTM*36:10109");
        assertEquals(segs[1], "LOC*31*USA");
        assertEquals(segs[2], "NAD*FL***ANDREWS:TIFFANY:PAGE");
        assertEquals(segs[3], "ATT*2**F");
    }

}

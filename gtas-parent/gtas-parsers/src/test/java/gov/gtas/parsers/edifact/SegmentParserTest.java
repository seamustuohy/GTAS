package gov.gtas.parsers.edifact;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SegmentParserTest {
    SegmentParser parser;
    
    @Before
    public void setUp() throws Exception {
        UNA una = new UNA();
        this.parser = new SegmentParser(una);
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testHappyPath() {
        String seg = "NAD+FL+++PAGE:TIFFANY:ANNE";
        Composite[] composites = parser.parseSegment(seg);
        assertEquals(composites.length, 5);
        assertEquals(composites[0].getValue(), "NAD");
        assertEquals(composites[1].getValue(), "FL");
        assertEquals(composites[2].getValue(), "");
        assertEquals(composites[3].getValue(), "");
        assertEquals(composites[4].getValue(), null);
        assertEquals(composites[4].getElements().length, 3);
        assertEquals(composites[4].getElements()[0].getValue(), "PAGE");
        assertEquals(composites[4].getElements()[1].getValue(), "TIFFANY");
        assertEquals(composites[4].getElements()[2].getValue(), "ANNE");
    }

    @Test
    public void testNullSegment() {
        Composite[] composites = parser.parseSegment(null);        
        assertTrue(composites == null);
    }

    @Test
    public void testSegmentNameOnly() {
        Composite[] composites = parser.parseSegment("NAD");        
        assertEquals(composites.length, 1);
        assertEquals(composites[0].getValue(), "NAD");
        assertTrue(composites[0].getElements() == null);
    }
    
    @Test
    public void testEscapedDelimiters() {
        String seg = "NAD+FL?+MC?:MD+++PAGE:TIFFANY:ANNE";
        Composite[] composites = parser.parseSegment(seg);
        assertEquals(composites.length, 5);
        assertEquals(composites[1].getValue(), "FL+MC:MD");
    }
}

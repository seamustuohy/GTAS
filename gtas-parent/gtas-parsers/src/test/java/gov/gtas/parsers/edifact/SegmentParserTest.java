package gov.gtas.parsers.edifact;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.gtas.parsers.edifact.segment.UNA;

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
    public void testNullOrEmpty() {
        Composite[] c = parser.parseSegment(null);
        assertNull(c);
        c = parser.parseSegment("");
        assertNull(c);
        c = parser.parseSegment("   ");
        assertNull(c);
    }
    
    @Test
    public void testHappyPath() {
        String seg = "NAD+FL+++PAGE:TIFFANY:ANNE";
        Composite[] composites = parser.parseSegment(seg);
        assertEquals(5, composites.length, 5);
        assertEquals("NAD", composites[0].getValue());
        assertEquals("FL", composites[1].getValue());
        assertEquals("", composites[2].getValue());
        assertEquals("", composites[3].getValue());
        assertEquals(null, composites[4].getValue());
        assertEquals(3, composites[4].getElements().length);
        assertEquals("PAGE", composites[4].getElements()[0].getValue());
        assertEquals("TIFFANY", composites[4].getElements()[1].getValue());
        assertEquals("ANNE", composites[4].getElements()[2].getValue());
    }

    @Test
    public void testSegmentNameOnly() {
        Composite[] composites = parser.parseSegment("NAD");        
        assertEquals(1, composites.length);
        assertEquals("NAD", composites[0].getValue());
        assertTrue(composites[0].getElements() == null);
    }
    
    @Test
    public void testEscapedDelimiters() {
        String seg = "NAD+FL?+MC?:MD+++PAGE:TIFFANY:ANNE";
        Composite[] composites = parser.parseSegment(seg);
        assertEquals(5, composites.length);
        assertEquals("FL+MC:MD", composites[1].getValue());
    }

}

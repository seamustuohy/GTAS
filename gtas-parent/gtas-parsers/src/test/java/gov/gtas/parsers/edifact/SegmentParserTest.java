package gov.gtas.parsers.edifact;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.gtas.parsers.edifact.segment.UNA;

public class SegmentParserTest {
    SegmentTokenizer parser;
    
    @Before
    public void setUp() throws Exception {
        UNA una = new UNA();
        this.parser = new SegmentTokenizer(una);
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testNullOrEmpty() {
        Composite[] c = parser.tokenize(null);
        assertNull(c);
        c = parser.tokenize("");
        assertNull(c);
        c = parser.tokenize("   ");
        assertNull(c);
    }
    
    @Test
    public void testHappyPath() {
        String seg = "NAD+FL+++PAGE:TIFFANY:ANNE";
        Composite[] composites = parser.tokenize(seg);
        assertEquals(5, composites.length, 5);
        assertEquals("NAD", composites[0].getElement(0));
        assertEquals("FL", composites[1].getElement(0));
        assertEquals("", composites[2].getElement(0));
        assertEquals("", composites[3].getElement(0));
        assertEquals(3, composites[4].numElements());
        assertEquals("PAGE", composites[4].getElement(0));
        assertEquals("TIFFANY", composites[4].getElement(1));
        assertEquals("ANNE", composites[4].getElement(2));
    }

    @Test
    public void testSegmentNameOnly() {
        Composite[] composites = parser.tokenize("NAD");        
        assertEquals(1, composites.length);
        assertEquals("NAD", composites[0].getElement(0));
    }
    
    @Test
    public void testEscapedDelimiters() {
        String seg = "NAD+FL?+MC?:MD+++PAGE:TIFFANY:ANNE";
        Composite[] composites = parser.tokenize(seg);
        assertEquals(5, composites.length);
        assertEquals("FL+MC:MD", composites[1].getElement(0));
    }

}

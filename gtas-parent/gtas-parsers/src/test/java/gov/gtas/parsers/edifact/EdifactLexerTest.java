package gov.gtas.parsers.edifact;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.LinkedList;

import org.junit.Test;

import gov.gtas.parsers.edifact.segment.UNA;

public class EdifactLexerTest {
    EdifactLexer parser = new EdifactLexer();
    
    /**
     * taken from https://en.wikipedia.org/wiki/EDIFACT
     */
    String test = "UNA:+.? '\r\n" + 
            "UNB+IATB:1+6XPPC+LHPPC+940101:0950+1'\r\n" + 
            "UNH+1+PAORES:93:1:IA'\r\n" + 
            "MSG+1:45'\r\n" + 
            "IFT+3+XYZCOMPANY AVAILABILITY'\r\n" + 
            "ERC+A7V:1:AMD'\r\n" + 
            "IFT+3+NO MORE FLIGHTS'\r\n" + 
            "ODI'\r\n" + 
            "TVL+240493:1000::1220+FRA+JFK+DL+400+C'\r\n" + 
            "PDI++C:3+Y::3+F::1'\r\n" + 
            "APD+74C:0:::6++++++6X'\r\n" + 
            "TVL+240493:1740::2030+JFK+MIA+DL+081+C'\r\n" + 
            "PDI++C:4'\r\n" + 
            "APD+EM2:0:1630::6+++++++DA'\r\n" + 
            "UNT+13+1'\r\n" + 
            "UNZ+1+1'";
    
    @Test
    public void testParse() throws ParseException {
        LinkedList<Segment> segments = parser.parse(test);
        assertEquals(15, segments.size());
        for (Segment s : segments) {
            // random checks
            Composite[] c = s.getComposites();
            switch(s.getName()) {
            case "UNH":
                assertEquals(2, c.length);
                assertEquals("1", c[0].getValue());
                assertNull(c[0].getElements());
                assertNull(c[1].getValue());
                assertEquals("PAORES", c[1].getElements()[0].getValue());
                break;
            case "ODI":
                assertNull(c);
                break;
            case "ERC":
                assertEquals(1, c.length);
                assertEquals("A7V", c[0].getElements()[0].getValue());
                break;
            }
            System.out.println(s);
        }
    }
    
    @Test
    public void testDefaultUna() {
        UNA una = new UNA();
        assertEquals(':', una.getComponentDataElementSeparator());
        assertEquals('+', una.getDataElementSeparator());
        assertEquals('.', una.getDecimalMark());
        assertEquals('?', una.getReleaseCharacter());
        assertEquals(' ', una.getRepetitionSeparator());
        assertEquals('\'', una.getSegmentTerminator());
    }
}

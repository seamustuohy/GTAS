package gov.gtas.parsers.pnrgov;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.segment.UNA;

public class PnrUtilsTest {

    @Test
    public void testgetSinglePnr() {
        String msg = "UNA:+.?*'\n" + 
                "UNB+IATA:1+DL++101209:2100+020A07'\n" + 
                "UNH+1+PNRGOV:10:1:IA+F6C2C268'\n" + 
                "MSG+:22'\n" + 
                "ORG+DL:ATL+52519950'\n" + 
                "TVL+121210:0915+LHR+JFK+DL+324'\n" + 
                "EQN+2'" + 
                "SRC'" + 
                "RCI+DL:MFN4TI1'" +
                "SRC'" + 
                "RCI+DL:MFN4TI2'" +
                "SRC'" + 
                "RCI+DL:MFN4TI3'" +
                "UNT+135+1'\n" + 
                "UNZ+1+020A07'";

        UNA una = EdifactLexer.getUnaSegment(msg);
        assertEquals(null, PnrUtils.getSinglePnr(null, una, -1));
        assertEquals(null, PnrUtils.getSinglePnr("", una, -1));
        assertEquals(null, PnrUtils.getSinglePnr("\n", una, -1));                
        assertEquals(null, PnrUtils.getSinglePnr(msg, una, -1));
        assertEquals("SRC'RCI+DL:MFN4TI1'", PnrUtils.getSinglePnr(msg, una, 0));
        assertEquals("SRC'RCI+DL:MFN4TI2'", PnrUtils.getSinglePnr(msg, una, 1));
        assertEquals("SRC'RCI+DL:MFN4TI3'", PnrUtils.getSinglePnr(msg, una, 2));
        assertEquals(null, PnrUtils.getSinglePnr(msg, una, 3));
    }
}

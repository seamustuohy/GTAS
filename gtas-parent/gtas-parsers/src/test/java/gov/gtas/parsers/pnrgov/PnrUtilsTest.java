package gov.gtas.parsers.pnrgov;

import org.junit.Test;

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
                "SRC'\n" + 
                "RCI+DL:MFN4TI1'" +
                "SRC'\n" + 
                "RCI+DL:MFN4TI2'" +
                "SRC'\n" + 
                "RCI+DL:MFN4TI3'" +
                "UNT+135+1'\n" + 
                "UNZ+1+020A07'";
                
        System.out.println(PnrUtils.getSinglePnr(msg, 0) + "\n");
        System.out.println(PnrUtils.getSinglePnr(msg, 1) + "\n");
        System.out.println(PnrUtils.getSinglePnr(msg, 2) + "\n");
        System.out.println(PnrUtils.getSinglePnr(msg, 3) + "\n");
    }
}

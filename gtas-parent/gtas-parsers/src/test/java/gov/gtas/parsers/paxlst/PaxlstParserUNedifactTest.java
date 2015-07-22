package gov.gtas.parsers.paxlst;

import java.text.ParseException;
import java.util.List;
import static org.junit.Assert.*;

import org.junit.Test;

import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;

public final class PaxlstParserUNedifactTest {
    PaxlstParser parser; 
    
    String header = 
            "UNA:+.? '" + 
            "UNB+UNOA:4+APIS*ABE+USADHS+070429:0900+000000001++USADHS'" + 
            "UNH+PAX001+PAXLST:D:05B:UN:IATA'" +
            "BGM+745'";
    
    @Test
    public void testSingleTdtWithOneLeg() throws ParseException {
        String apis = header + 
                "TDT+20+UA123+++UA'" + 
                "LOC+125+YVR'" + 
                "DTM+189:0704291230:201'" + 
                "LOC+87+JFK'" + 
                "DTM+232:0704291600:201'";

        parser = new PaxlstParserUNedifact(apis);
        ApisMessageVo vo = parser.parse();
        List<FlightVo> flights = vo.getFlights();
        assertEquals(1, flights.size());
        FlightVo f = flights.get(0);
        assertEquals("123", f.getFlightNumber());
        assertEquals("YVR", f.getOrigin());
        assertEquals("JFK", f.getDestination());
    }

    @Test
    public void testMultipleTdtWithOneLegEach() throws ParseException {
        String apis = header + 
                "TDT+20+UA123+++UA'" + 
                "LOC+125+YVR'" + 
                "DTM+189:0704291230:201'" + 
                "LOC+87+JFK'" + 
                "DTM+232:0704291600:201'" + 
                "TDT+20+UA124+++UA'" + 
                "LOC+92+JFK'" + 
                "DTM+189:0704291730:201'" + 
                "LOC+92+ATL'" + 
                "DTM+232:0704291945:201'";

        parser = new PaxlstParserUNedifact(apis);
        ApisMessageVo vo = parser.parse();
        List<FlightVo> flights = vo.getFlights();
        assertEquals(2, flights.size());
        FlightVo f1 = flights.get(0);
        assertEquals("123", f1.getFlightNumber());
        assertEquals("YVR", f1.getOrigin());
        assertEquals("JFK", f1.getDestination());
        FlightVo f2 = flights.get(1);
        assertEquals("124", f2.getFlightNumber());
        assertEquals("JFK", f2.getOrigin());
        assertEquals("ATL", f2.getDestination());
    }
    
    @Test
    public void testMultipleTdtWithLoc92() throws ParseException {
        String apis = header + 
                "TDT+20+KE250+++KE'" +
                "LOC+92+JFK'" +
                "DTM+189:1402010220:201'" +
                "LOC+92+ANC'" +
                "DTM+232:1402010550:201'" + 
                "TDT+20+KE250+++KE'" +
                "LOC+125+ANC'" +
                "DTM+189:1402010650:201'" +
                "LOC+87+ICN'" +
                "DTM+232:1402020840:201'";
        
        parser = new PaxlstParserUNedifact(apis);
        ApisMessageVo vo = parser.parse();
        List<FlightVo> flights = vo.getFlights();
        assertEquals(2, flights.size());
        
        FlightVo f1 = flights.get(0);
        assertEquals("250", f1.getFlightNumber());
        assertEquals("JFK", f1.getOrigin());
        assertEquals("ANC", f1.getDestination());
        
        FlightVo f2 = flights.get(1);
        assertEquals("250", f2.getFlightNumber());
        assertEquals("ANC", f2.getOrigin());
        assertEquals("ICN", f2.getDestination());
    } 

    /**
     * Should produce the exact same output as testMultipleTdtWithLoc92
     */
    @Test
    public void testSingleTdtWithLoc92() throws ParseException {
        String apis = header +
                "TDT+20+KE250+++KE'" +
                "LOC+92+JFK'" +
                "DTM+189:1402010220:201'" +
                "LOC+92+ANC'" +
                "DTM+232:1402010550:201'" + 
                "LOC+125+ANC'" +
                "DTM+189:1402010650:201'" +
                "LOC+87+ICN'" +
                "DTM+232:1402020840:201'";
        
        parser = new PaxlstParserUNedifact(apis);
        ApisMessageVo vo = parser.parse();
        List<FlightVo> flights = vo.getFlights();
        assertEquals(2, flights.size());
        
        FlightVo f1 = flights.get(0);
        assertEquals("250", f1.getFlightNumber());
        assertEquals("JFK", f1.getOrigin());
        assertEquals("ANC", f1.getDestination());
        
        FlightVo f2 = flights.get(1);
        assertEquals("250", f2.getFlightNumber());
        assertEquals("ANC", f2.getOrigin());
        assertEquals("ICN", f2.getDestination());        
    } 

//    @Test
//    public void testWithLoc130() throws ParseException {
///*
// * LOC+125+LHR' Indicates the last airport of departure from a foreign country, i.e. London Heathrow
//LOC+87+YUL' Indicates the first airport of arrival in the country of destination, i.e. Montreal Dorval
//21
//￼
//LOC+92+YOW' Indicates the next airport in the country of destination, i.e. Ottawa International
//LOC+130+YVR' Indicates the final destination airport in the country of destination, i.e.         
// */
//        
//    }
   
    @Test
    public void testRandomApis() throws ParseException {
        String apis = "UNA:+.?*'" + 
                "UNB+UNOA:4+ZZAIR+DHS+080708:0545+000000001++DHS' " + 
                "UNG+PAXLST+ZZAIR+ DHS+080708:0545+1+UN+D:05B' " + 
                "UNH+PAX001+PAXLST:D:05B:UN:IATA' " + 
                "BGM+745+CP'" + 
                "RFF+AVF:A1B2C3'" + 
                "DTM+179:20080610'" + 
                "DTM+558:20080612'" + 
                "DTM+183:20080630'" +
                "NAD+MS+++C UNDERWOOD'" + 
                "COM+703-555-1212:TE+703-555-4545:FX' " + 
                "NAD+BA+ORION TRAVEL++J JAVERT'" + 
                "NAD+AG+ORION TRAVEL++D SONET'" + 
                "TDT+20+QQ827+++QQ'" + 
                "LOC+92+VIE'" + 
                "DTM+189:200807102140:201'" + 
                "LOC+92+BRU'" + 
                "DTM+232:200807102355:201'" + 
                "TDT+20+ QQ827+++QQ'" + 
                "LOC+92+BRU'" + 
                "DTM+189:200807102140:201'" + 
                "LOC+92+CDG'" + 
                "DTM+232:200807102300:201'" + 
                "TDT+20+QQ827+++QQ '" + 
                "LOC+125+CDG'" + 
                "DTM+189:200807110200:201'" + 
                "LOC+87+IAD'" + 
                "DTM+232:200807111035:201'" + 
                "NAD+FL+++BARRE:TODD+123 E MAIN ST+STAFFORD+VA+22554+USA'" + 
                "ATT+2++M'" + 
                "DTM+329:680223'" + 
                "GEI+4+ZZZ:::HK'" + 
                "LOC+178+VIE'" + 
                "LOC+179+IAD'" + 
                "LOC+174+FRA'" + 
                "LOC+22+IAD'" + 
                "COM+55 121212:TE+033:AI'" + 
                "NAT+2+FRA'" + 
                "RFF+AVF:A1B2C3'" + 
                "RFF+ABO:UUI34T543'" + 
                "RFF+SEA:35F'" + 
                "RFF+ACD:AA1234567890'" + 
                "RFF+CBA:09-87-654321'" + 
                "DOC+P+YY3478621G'" + 
                "DTM+36:081230'" + 
                "LOC+91+FRA:::PARIS'" + 
                "NAD+FL+++LANG:KRISTIN+123 E MAIN ST+ STAFFORD+VA+22554+USA'" + 
                "ATT+2++F'" + 
                "DTM+329:700606'" + 
                "GEI+4+ZZZ:::HK'" + 
                "LOC+178+VIE'" + 
                "LOC+179+IAD'" + 
                "LOC+174+ESP'" + 
                "COM+55 343434:TE+033:AI'" + 
                "NAT+2+ESP'" + 
                "RFF+AVF:A1B2C3'" + 
                "RFF+ABO:WWT098L31E'" + 
                "RFF+SEA:35E'" + 
                "RFF+CBA:09-87-654325'" + 
                "DOC+P+YY3478621G'" + 
                "DTM+36:081230'" + 
                "LOC+91+ESP'" + 
                "CNT+42:2'" + 
                "CNT+59:3'" + 
                "FTX+OSI+++CTCA 703-555-1212 STEVEN WILSON TVL/X33566153/JOESEPH'" + 
                "FTX+OSI+++CTCT NYC 888-555-1111 COOL TRAVEL SCA I 12 234234'" + 
                "FTX+SSR+++FQTVQQHK1/ QQ8270000/BARRE/TODDMR'" + 
                "FTX+SSR+++TKNEBAHK2/ QQ8270000/LANG/KRISTINMS'" + 
                "FTX+SSR+++PLS SEE QQ SEAT POLICY - SEAT REQUEST NOT PERMITTED'" + 
                "FTX+HST+C++EI 166 Y 08JUL10 VIEBRU HK1 1340 1555/HK'" + 
                "FTX+PMT++AB+300, AV SUFFREN 75015 Paris, France'" + 
                "FTX+PMT++TYP+CASH'" + 
                "FTX+MKS+++QQ-123-11111111'" + 
                "FTX+MKS+++QQ-123-22222222'" + 
                "FTX+MKS+++QQ-123-33333333'" + 
                "FTX+AAI+++VALUED PASSENGER'" + 
                "UNT+78+PAX001' " + 
                "UNE+1+1' " + 
                "UNZ+1+000000001'";
        
        parser = new PaxlstParserUNedifact(apis);
        ApisMessageVo vo = parser.parse();
        List<FlightVo> flights = vo.getFlights();
        assertEquals(3, flights.size());
    }
}

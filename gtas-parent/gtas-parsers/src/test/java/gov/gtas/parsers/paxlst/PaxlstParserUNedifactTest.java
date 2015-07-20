package gov.gtas.parsers.paxlst;

import java.text.ParseException;
import java.util.List;
import static org.junit.Assert.*;

import org.junit.Test;

import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;

public final class PaxlstParserUNedifactTest {
    PaxlstParser parser; 
    
    @Test
    public void testSingleTdtWithOneLeg() throws ParseException {
        String apis = "UNA:+.? '" + 
                "UNB+UNOA:4+APIS*ABE+USADHS+070429:0900+000000001++USADHS'" + 
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
        String apis = "UNA:+.? '" + 
                "UNB+UNOA:4+APIS*ABE+USADHS+070429:0900+000000001++USADHS'" + 
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
        String apis = "UNA:+.? '" + 
                "UNB+UNOA:4+APIS*ABE+USADHS+070429:0900+000000001++USADHS'" + 
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
        String apis = "UNA:+.? '" + 
                "UNB+UNOA:4+APIS*ABE+USADHS+070429:0900+000000001++USADHS'" + 
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

    @Test
    public void testWithLoc130() throws ParseException {
/*
 * LOC+125+LHR' Indicates the last airport of departure from a foreign country, i.e. London Heathrow
LOC+87+YUL' Indicates the first airport of arrival in the country of destination, i.e. Montreal Dorval
21
￼
LOC+92+YOW' Indicates the next airport in the country of destination, i.e. Ottawa International
LOC+130+YVR' Indicates the final destination airport in the country of destination, i.e.         
 */
        
    }
}

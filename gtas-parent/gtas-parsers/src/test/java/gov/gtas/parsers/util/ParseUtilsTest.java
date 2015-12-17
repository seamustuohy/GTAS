package gov.gtas.parsers.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import gov.gtas.parsers.exception.ParseException;

public class ParseUtilsTest {
    @Test
    public void testSeparateCarrierAndFlightNumber() {
        String[] tests = {
                "UA0341",
                "UA123",
                "Z445",
                "3Z1"
        };
        
        String[][] expected = {
                {"UA", "0341"},
                {"UA", "123"},
                {"Z4", "45"},
                {"3Z", "1"}
        };
        
        int i = 0;
        for (String s : tests) {
            String[] actual = ParseUtils.separateCarrierAndFlightNumber(s);
            assertEquals(2, actual.length);
            assertTrue(Arrays.deepEquals(expected[i], actual));
            i++;
        }
    }
    
    @Test
    public void testCalculateAge() throws ParseException {
        Date d = ParseUtils.parseDateTime("03051980", "MMddyyyy");
        int age = DateUtils.calculateAge(d);
        System.out.println(d + " " + age);
    }
}

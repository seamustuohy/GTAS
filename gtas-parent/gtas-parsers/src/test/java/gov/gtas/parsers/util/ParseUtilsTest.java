package gov.gtas.parsers.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import gov.gtas.parsers.exception.ParseException;

public class ParseUtilsTest {
    @Test
    public void testCalculateAge() throws ParseException {
        Date d = ParseUtils.parseDateTime("03051980", "MMddyyyy");
        int age = DateUtils.calculateAge(d);
        System.out.println(d + " " + age);
    }
}

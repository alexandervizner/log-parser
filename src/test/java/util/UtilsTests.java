package util;

import org.homebrewed.timing_report.util.Utils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UtilsTests {

    @Test
    public void shouldReturnTrueIfStringIsEmpty() {
        assertTrue(Utils.isEmpty(null));
        assertTrue(Utils.isEmpty(""));
        assertTrue(Utils.isEmpty(" "));
    }

    @Test
    public void shouldReturnFalseIfStringNotEmpty() {
        assertFalse(Utils.isEmpty("test"));
    }


    @Test
    public void shouldReturnTrue() {
        assertTrue(Utils.booleanize("true"));
        assertTrue(Utils.booleanize("True"));
        assertTrue(Utils.booleanize("TRUE"));
        assertTrue(Utils.booleanize("Y"));
        assertTrue(Utils.booleanize("y"));
    }

    @Test
    public void shouldReturnFalse() {
        assertFalse(Utils.booleanize("false"));
        assertFalse(Utils.booleanize("False"));
        assertFalse(Utils.booleanize("FALSE"));
        assertFalse(Utils.booleanize("n"));
        assertFalse(Utils.booleanize("N"));
    }

    @Test
    public void shouldReturnTokensWithTimestamp() {
        String line = "2015-08-19 00:06:28,957 (http--0.0.0.0-28080-405) [CUST:CUS88O8888] /chat.do in 2";
        String[] tokens = Utils.sanitizeAndSplit(line);

        assertEquals(6, tokens.length);
        assertEquals("2015-08-19T00:06:28,957", tokens[0]);
        assertEquals("2", tokens[tokens.length -1]);
    }

    @Test
    public void shouldParseDateCorrectly() {
        Date expectedDate = new Date();
        expectedDate.setTime(1439931988957L);
        Date date = Utils.toDatetime("2015-08-19T00:06:28,957");

        assertEquals(0, date.compareTo(expectedDate));
    }

    @Test
    public void should() {
        Date date = Utils.toDatetime("2015-08-19T00:06:28,957");
        Date expectedDate = Utils.toDatetime("2015-08-19T00:59:59,999");
        Calendar dateUnderTest = Utils.getEndOfHour(date);

        assertEquals(expectedDate.getTime(), dateUnderTest.getTime().getTime());
    }

}

package matcher;

import org.homebrewed.timing_report.matcher.DateTimeMatcher;
import org.homebrewed.timing_report.util.Constants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeMatcherTests {

    private DateTimeMatcher dateTimeMatcher;

    @Before
    public void setup() {
        dateTimeMatcher = new DateTimeMatcher(Constants.DEFAULT_DATE_FORMAT);
    }

    @Test
    public void shouldValidate() {
        assertTrue(dateTimeMatcher.validate("2015-08-19T00:01:59,769"));
    }

    @Test
    public void shouldInvalidate() {
        assertFalse(dateTimeMatcher.validate("2015-08-19T00:01:59:769"));
    }

    @Test
    public void shouldReturnSimpleClassName() {
        assertEquals("DateTimeMatcher", dateTimeMatcher.getName());
    }

    @Test
    public void shouldReturnValidatedDate() {
        dateTimeMatcher.validate("2015-08-19T00:01:59,769");
        assertEquals("2015-08-19T00:01:59,769", dateTimeMatcher.toString());
    }
}

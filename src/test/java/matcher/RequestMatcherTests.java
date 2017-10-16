package matcher;

import org.homebrewed.timing_report.matcher.RequestMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestMatcherTests {

    private RequestMatcher requestMatcher;

    @Before
    public void setup() {
        requestMatcher = new RequestMatcher("/");
    }

    @Test
    public void shouldValidate() {
        assertTrue(requestMatcher.validate("/index.htm"));
    }

    @Test
    public void shouldInvalidate() {
        assertFalse(requestMatcher.validate("updateOrders"));
    }

    @Test
    public void shouldReturnSimpleClassName() {
        assertEquals("RequestMatcher", requestMatcher.getName());
    }

    @Test
    public void shouldReturnValidatedDate() {
        String req = "/api/v1/order/1";
        requestMatcher.validate(req);
        assertEquals(req, requestMatcher.toString());
    }
}

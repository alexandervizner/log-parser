package matcher;

import org.homebrewed.timing_report.matcher.ResourceMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceMatcherTests {

    private ResourceMatcher resourceMatcher;

    @Before
    public void setup() {
        resourceMatcher = new ResourceMatcher("contentId");
    }

    @Test
    public void shouldValidate() {
        assertTrue(resourceMatcher.validate("/mainContent.do?action=CAROUSEL&contentId=main_carousel"));
    }

    @Test
    public void shouldInvalidate_1() {
        assertFalse(resourceMatcher.validate("/index.html"));
    }

    @Test
    public void shouldInvalidate_2() {
        assertFalse(resourceMatcher.validate("getMoreTests"));
    }
}

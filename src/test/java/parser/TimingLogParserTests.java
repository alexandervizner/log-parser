package parser;

import org.homebrewed.timing_report.matcher.DateTimeMatcher;
import org.homebrewed.timing_report.matcher.DurationMatcher;
import org.homebrewed.timing_report.matcher.RequestMatcher;
import org.homebrewed.timing_report.matcher.ResourceMatcher;
import org.homebrewed.timing_report.parser.TimingLogParser;
import org.homebrewed.timing_report.util.Constants;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class TimingLogParserTests {

    private static final String LOG = "2015-08-19 00:06:27,877 (http--0.0.0.0-28080-259) [CUST:CUS88O8888] /bbBillingData.do in 1\n" +
            "2015-08-19 00:06:27,879 (http--0.0.0.0-28080-407) [CUST:CUS88O8888] /kkkBillingData.do in 1\n" +
            "2015-08-19 00:06:27,882 (http--0.0.0.0-28080-297) [CUST:CUS88O8888] /yyyBillingData.do in 1\n" +
            "2015-08-19 00:06:27,882 (http--0.0.0.0-28080-405) [CUST:CUS88O8888] /uutBillingData.do in 0\n" +
            "2015-08-19 00:06:28,527 (http--0.0.0.0-28080-259) [CUST:CUS88O8888] /mainContent.do?action=SUBSCRIPTION&msisdn=300316597768 in 16\n" +
            "2015-08-19 00:06:28,587 (http--0.0.0.0-28080-391) [CUST:CUS99P9988] /mainContent.do?action=SUBSCRIPTION&msisdn=300135411231 in 15\n" +
            "2015-08-19 00:06:28,957 (http--0.0.0.0-28080-405) [CUST:CUS88O8888] /chat.do in 2";

    private TimingLogParser parser;

    @Before
    public void setup() {
        parser = new TimingLogParser(
                new DateTimeMatcher(Constants.DEFAULT_DATE_FORMAT),
                new ResourceMatcher("msisdn"),
                new DurationMatcher(5000));
    }

    @Test
    public void shouldParseLogSuccesfully() throws UnsupportedEncodingException {
        InputStream stream = new ByteArrayInputStream(LOG.getBytes(StandardCharsets.UTF_8.name()));
        parser.parse(new Scanner(stream));
        assertEquals(2, parser.getAvgReqDurationResults().size());
        assertEquals(6, parser.getReqPerHourResults().get(0).getRequestsCounter());
        assertEquals(LOG.split("\n").length, parser.getLinesProcessed());
    }
}

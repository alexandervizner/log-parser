package org.homebrewed.timing_report.statistic;

import org.homebrewed.timing_report.matcher.DateTimeMatcher;
import org.homebrewed.timing_report.matcher.DurationMatcher;
import org.homebrewed.timing_report.matcher.Matcher;
import org.homebrewed.timing_report.matcher.ResourceMatcher;

import java.util.Date;
import java.util.List;

/**
 * Average durations of request statistic container
 */
public class RequestsAverageDurationStatistic {

    private Date date;
    private Integer duration;
    private String resource;

    public RequestsAverageDurationStatistic() {
    }

    public RequestsAverageDurationStatistic(List<Matcher> matchers) {

        for (Matcher matcher : matchers) {
            if (matcher instanceof DateTimeMatcher) {
                this.date = (Date) matcher.getResult();
            } else if (matcher instanceof ResourceMatcher) {
                this.resource = (String) matcher.getResult();
            } else if (matcher instanceof DurationMatcher) {
                this.duration = (Integer) matcher.getResult();
            }
        }
    }

    public Integer getDuration() {
        return this.duration;
    }

    public String getResource() {
        return this.resource;
    }

    public Date getDate() {
        return this.date;
    }
}

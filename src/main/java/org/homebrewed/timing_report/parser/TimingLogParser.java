package org.homebrewed.timing_report.parser;

import org.homebrewed.timing_report.matcher.Matcher;
import org.homebrewed.timing_report.matcher.RequestMatcher;
import org.homebrewed.timing_report.statistic.RequestsAverageDurationStatistic;
import org.homebrewed.timing_report.statistic.RequestsHourlyStatistic;
import org.homebrewed.timing_report.statistic.Statistic;
import org.homebrewed.timing_report.util.Utils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Timing log parser gathers statistics data for 'Requests per hour' and 'Average duration of request' reports.
 * The parser processes log entry in format '2015-08-19 10:06:31,997 (http--0.0.0.0-28080-3) [] /checkSession.do in 122'
 * Each token of the string is analyzed by Matcher that corresponds to its type (datetime, url, numeric. etc)
 *
 * @see org.homebrewed.timing_report.report.RequestsHourlyReport
 * @see org.homebrewed.timing_report.report.RequestsAverageDurationReport
 * @see org.homebrewed.timing_report.matcher.Matcher
 */
public class TimingLogParser implements Parser {

    private List<Matcher> matchers = new ArrayList<>();

    public TimingLogParser(Matcher... matchers) {
        this.matchers = Arrays.asList(matchers);
    }

    public void addMatcher(Matcher matcher) {
        this.matchers.add(matcher);
    }

    public List<Matcher> getMatchers() {
        return this.matchers;
    }

    private List<RequestsAverageDurationStatistic> ardStatsData = new ArrayList<>();
    private List<RequestsHourlyStatistic> rhStatsData = new ArrayList<>();
    private int linesProcessed = 0;

    @Override
    public void parse(Scanner scanner) {

        Calendar clr;
        boolean recalculate = true;
        RequestMatcher rm = new RequestMatcher("/");
        RequestsHourlyStatistic rhStats = new RequestsHourlyStatistic();

        while (scanner.hasNextLine()) {
            ++linesProcessed;
            String line = scanner.nextLine();
            String[] tokens = Utils.sanitizeAndSplit(line);
            List<Matcher> matches = new ArrayList<>();

            if (tokens.length < matchers.size()) {
                continue;
            }

            // Separate counting of requests per hr
            if (rm.validate(tokens[3])) {

                Date date = Utils.toDatetime(tokens[0]);

                if (recalculate) {
                    recalculate = false;
                    clr = Utils.getEndOfHour(date);
                    rhStats.setDateTime(clr.getTime());
                }

                if (date.before(rhStats.getDateTime()) && scanner.hasNextLine()) {
                    rhStats.increaseRequestCounter();
                } else {

                    // Store statistics for current hour frame and initialize fresh statistic obj
                    rhStatsData.add(rhStats);

                    // But anyway still needed to do something with new line we got
                    // Special case when only single request per hour happened
                    rhStats = new RequestsHourlyStatistic();
                    rhStats.increaseRequestCounter();
                    clr = Utils.getEndOfHour(date);
                    rhStats.setDateTime(clr.getTime());
                }
            }

            int i = 0;

            // First analyze all the strings parts for matches
            for (String token : tokens) {
                for (Matcher matcher : this.matchers.subList(i, this.matchers.size())) {
                    if (matcher.validate(token)) {
                        matches.add(matcher);

                        // Keeping in mind optimistic assumption that line's tokens wont be reodered
                        i = this.matchers.indexOf(matcher) + 1;
                        break;
                    }
                }
            }

            // Finally check the line fills the requirements of all matchers
            if (matches.size() >= 3) {
                Utils.debug("Line '" + line + "' processed with matches: "
                        + matches.stream().map(Matcher::toString).collect(Collectors.joining(", ")));
                ardStatsData.add(new RequestsAverageDurationStatistic(matches));
                matches.clear();
            }
        }

        Utils.debug("Count successful matches to be processed: " + ardStatsData.size());
    }

    @Override
    public List<Statistic> getResults() {
         return null;
    }

    public List<RequestsAverageDurationStatistic> getAvgReqDurationResults() {
        return ardStatsData;
    }

    public List<RequestsHourlyStatistic> getReqPerHourResults() {
        return rhStatsData;
    }

    public int getLinesProcessed() {
        return linesProcessed;
    }
}

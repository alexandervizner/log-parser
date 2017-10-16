package org.homebrewed.timing_report.report;

import org.homebrewed.timing_report.statistic.RequestsAverageDurationStatistic;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Average duration of request report
 */
public class RequestsAverageDurationReport implements Report {

    private int top;
    private List<RequestsAverageDurationStatistic> statistics;
    private Map<String, Integer> results;
    private Map<String, Integer> sortedLimited;

    public RequestsAverageDurationReport(List<RequestsAverageDurationStatistic> statistics) {
        this.statistics = statistics;
        this.results = new HashMap<>();
        this.sortedLimited = new HashMap<>();
    }

    @Override
    public void outputTo(PrintStream stream) {

        int c = 1;
        PrintWriter writer = new PrintWriter(stream);

        if (sortedLimited.size() == 0) {
            writer.println("No data to show.");
        } else {
            writer.println("\nTOP " + this.top + " resources with longest average duration of request (in ms).\n");
            for (Map.Entry<String, Integer> entry : sortedLimited.entrySet()) {
                writer.println(c++ + ". Resource: '" + entry.getKey() + "', average request duration: " + entry.getValue());
            }
        }

        writer.println();
        writer.flush();
    }

    public RequestsAverageDurationReport calculate() {
        if (this.statistics.size() > 0) {

            // Aggregate duration values
            for (RequestsAverageDurationStatistic statistic : this.statistics) {
                results.merge(statistic.getResource(), statistic.getDuration(), Integer::sum);
            }
        }

        return this;
    }

    public RequestsAverageDurationReport forTop(int n) {
        sortedLimited = results.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        this.top = n;
        return this;
    }

}

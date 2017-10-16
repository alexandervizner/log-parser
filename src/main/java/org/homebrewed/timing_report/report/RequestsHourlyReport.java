package org.homebrewed.timing_report.report;

import org.homebrewed.timing_report.statistic.RequestsHourlyStatistic;

import java.io.PrintStream;
import java.util.List;

/**
 * Requests per hour report
 */
public class RequestsHourlyReport implements Report {

    private List<RequestsHourlyStatistic> statistics;
    private String bar;
    private String representation;
    private int scale;

    private static final String DEFAULT_BAR = "|";
    private static final int DEFAULT_SCALE = 10;

    public RequestsHourlyReport(List<RequestsHourlyStatistic> statistics) {
        this(statistics, DEFAULT_BAR, DEFAULT_SCALE);
    }

    public RequestsHourlyReport(List<RequestsHourlyStatistic> statistics, String bar) {
        this(statistics, bar, DEFAULT_SCALE);
    }

    public RequestsHourlyReport(List<RequestsHourlyStatistic> statistics, String bar, int scale) {
        this.statistics = statistics;
        this.bar = bar;
        this.scale = scale;
    }

    @Override
    public void outputTo(PrintStream stream) {

        if (this.representation == null) {
            calculate();
        }

        stream.println(this.representation);
    }

    @Override
    public RequestsHourlyReport calculate() {

        StringBuilder sb = new StringBuilder();
        sb.append("\nNumber of requests per hour (")
                .append((this.scale == 1) ? "each bar = 1 req" : "scaled by " + this.scale)
                .append(").\n\n");

        for (RequestsHourlyStatistic statistic : statistics) {
            sb.append(statistic.getDateTime()).append(": ");
            int scaled = (int) Math.ceil(statistic.getRequestsCounter() / scale);
            for (int j = 0; j < scaled; j++) {
                sb.append(this.bar);
            }
            sb.append(" (").append(statistic.getRequestsCounter()).append(")\n");
        }

        this.representation = sb.toString();
        return this;
    }
}

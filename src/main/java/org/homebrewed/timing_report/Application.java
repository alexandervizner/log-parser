package org.homebrewed.timing_report;

import org.homebrewed.timing_report.matcher.DateTimeMatcher;
import org.homebrewed.timing_report.matcher.DurationMatcher;
import org.homebrewed.timing_report.matcher.Matcher;
import org.homebrewed.timing_report.matcher.ResourceMatcher;
import org.homebrewed.timing_report.parser.TimingLogParser;
import org.homebrewed.timing_report.report.RequestsAverageDurationReport;
import org.homebrewed.timing_report.report.RequestsHourlyReport;
import org.homebrewed.timing_report.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.homebrewed.timing_report.util.Constants.*;
import static org.homebrewed.timing_report.util.Utils.debug;

public class Application {

    private static int N = 1;
    private static int N_MAX = DEFAULT_N_MAX;
    private static int TIMEOUT = DEFAULT_TIMEOUT;
    private static String[] KEYWORDS = DEFAULT_KEYWORDS;
    private static String DATETIME_FORMAT = DEFAULT_DATE_FORMAT;
    private static String FILENAME = DEFAULT_FILENAME;
    public static boolean DEBUG = DEFAULT_DEBUG;

    public static void main(String... args) {

        // Read application config
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream is = loader.getResourceAsStream(APP_CONFIG_FILENAME)) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        String debug = properties.getProperty(APP_DEBUG_PARAM_NAME);
        if (!Utils.isEmpty(debug)) {
            DEBUG = Utils.booleanize(debug);
        }

        String top_max = properties.getProperty(APP_TOP_MAX_PARAM_NAME);
        if (!Utils.isEmpty(top_max)) {
            N_MAX = Integer.parseInt(top_max);
        }
        sb.append("\t").append(APP_TOP_MAX_PARAM_NAME).append(": ").append(N_MAX).append("\n");

        String keywords = properties.getProperty(APP_KEYWORDS_PARAM_NAME);
        if (!Utils.isEmpty(keywords)) {
            KEYWORDS = keywords.trim().split(",");
        }
        sb.append("\t").append(APP_KEYWORDS_PARAM_NAME).append(": ").append(String.join(",", KEYWORDS)).append("\n");

        String timeout = properties.getProperty(APP_TIMEOUT_PARAM_NAME);
        if (!Utils.isEmpty(timeout)) {
            TIMEOUT = Integer.parseInt(timeout);
        }
        sb.append("\t").append(APP_TIMEOUT_PARAM_NAME).append(": ").append(TIMEOUT).append("\n");

        String datetime_format = properties.getProperty(APP_DATETIME_FORMAT_PARAM_NAME);
        if (!Utils.isEmpty(datetime_format)) {
            DATETIME_FORMAT = datetime_format;
        }
        sb.append("\t").append(APP_DATETIME_FORMAT_PARAM_NAME).append(": ").append(DATETIME_FORMAT).append("\n");

        // Process CLI arguments
        if (args.length == 0) {
            Utils.printUsageMessageAndExit(N_MAX);
        }

        boolean skip = false;
        for (String arg : args) {

            // Optional args
            if (arg.startsWith("-")) {
                if (arg.contains("h")) {
                    Utils.printUsageMessageAndExit(N_MAX);
                }
                if (arg.contains("d")) {
                    DEBUG = true;
                    continue;
                }
            }

            // Obligatory args
            if (!skip) {
                File file = new File(arg);
                if (!file.exists() || file.isDirectory()) {
                    Utils.printMessageAndExitWithCode("File name is not exists or its name is incorrect.", 1);
                } else {
                    FILENAME = arg;
                    skip = true;
                    continue;
                }
            }

            try {
                int n = Integer.parseInt(arg);
                if (n < N || n > N_MAX) {
                    Utils.printMessageAndExitWithCode("Number of resources is out of range: "
                            + N + "..." + N_MAX, 1);
                }
                N = n;
            } catch (NumberFormatException e) {
                Utils.printMessageAndExit("Incorrect number value.");
            }
        }

        sb.append("\t").append(APP_DEBUG_PARAM_NAME).append(": ").append(DEBUG).append("\n");
        sb.append("\t").append("TOP N value").append(": ").append(N).append("\n");
        sb.append("\t").append("File to parsing").append(": ").append(FILENAME).append("\n");

        debug("Loading application config from file: " + APP_CONFIG_FILENAME);
        debug("Starting application with settings: \n" + sb.toString());

        // Parse timing log
        long start = System.nanoTime();
        TimingLogParser parser = new TimingLogParser(
                new DateTimeMatcher(DATETIME_FORMAT),
                new ResourceMatcher(KEYWORDS),
                new DurationMatcher(TIMEOUT));

        debug("Initializing parser with matchers: "
                + String.join(", ", parser.getMatchers().stream().map(Matcher::getName).collect(toList())));

        try (Scanner scanner = new Scanner(new FileInputStream(FILENAME), "UTF-8")) {
            parser.parse(scanner);
        } catch (IOException e) {
            Utils.printMessageAndExitWithCode("Exception: " + e.getMessage(), 1);
        }

        // Process data
        RequestsAverageDurationReport radReport = new RequestsAverageDurationReport(parser.getAvgReqDurationResults());
        RequestsHourlyReport rhReport = new RequestsHourlyReport(parser.getReqPerHourResults());

        // Aggregating, calculating for top n resources and output in fancy manner
        radReport.calculate().forTop(N).outputTo(System.out);
        rhReport.calculate().outputTo(System.out);

        long et = TimeUnit.MILLISECONDS.convert((System.nanoTime() - start), TimeUnit.NANOSECONDS);
        System.out.println("Lines processed: " + parser.getLinesProcessed() + ", in " + et + " ms.");
    }

    public static void readConfigFromFile(String configName) {
        if (!Utils.isEmpty(configName)) {

        }
    }
}

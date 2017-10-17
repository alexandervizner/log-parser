package org.homebrewed.timing_report.util;

import org.homebrewed.timing_report.Application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.homebrewed.timing_report.util.Constants.DEFAULT_DATE_FORMAT;
import static org.homebrewed.timing_report.util.Constants.DEFAULT_TOKENS_DELIMITER;

public class Utils {

    /**
     * Prints to stdout debugging info if DEBUG_MODE is true.
     * This setting can be configured in application.properties file.
     */
    public static void debug(String msg) {
        if (Application.DEBUG) {
            System.out.println(new Date() + ": " + msg);
        }
    }

    private static final String NL = System.getProperty("line.separator");

    public static void printUsageMessageAndExit(int toMax) {
        System.out.print("NAME" + NL
                + "\ttiming-report -- parse, analyze the timing log and print results to stdout" + NL + NL
                + "DESCRIPTION" + NL
                + "\tThe utility parses the log file and extracts, aggregates and presents the data from it." + NL + NL
                + "USAGE" + NL
                + "\tjava -jar timing-report.jar [-hd] file N" + NL + NL
                + "\tfile : full or relative path to log file" + NL
                + "\td : print gobs of debugging information" + NL
                + "\th : print this help message" + NL
                + "\tN : positive number in range 1.." + toMax +
                " of resources with highest average request duration" + NL
        );
        System.exit(0);
    }

    public static void printMessageAndExit(String msg) {
        printMessageAndExitWithCode(msg, 0);
    }

    public static void printMessageAndExitWithCode(String msg, int exitCode) {
        System.out.println(msg);
        System.exit(exitCode);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.trim().length() == 0;

    }

    public static Map<String, Integer> sortResults(Map<String, Integer> unsorted, boolean reversed) {
        return unsorted.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(reversed ? Comparator.reverseOrder() : Comparator.naturalOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    /**
     * Sanitize input string and combine date and timestamp
     *
     * @param in input string
     * @return array of strings
     */
    public static String[] sanitizeAndSplit(String in) {

        if (isEmpty(in)) {
            return new String[0];
        }

        String stringFlattenDateTime = in.replaceFirst(" ", "T");

        return stringFlattenDateTime.trim().split(DEFAULT_TOKENS_DELIMITER);
    }

    public static boolean booleanize(String str) {
        return !isEmpty(str) && (str.trim().equalsIgnoreCase("true") || str.trim().equalsIgnoreCase("y"));

    }

    public static Date toDatetime(String datetime) {
        try {
            DateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
            return df.parse(datetime);
        } catch (ParseException ignored) {

        }
        return null;
    }

    public static Calendar getEndOfHour(Date date) {
        Calendar clr = Calendar.getInstance();
        clr.setTime(date);
        clr.set(Calendar.MINUTE, 59);
        clr.set(Calendar.SECOND, 59);
        clr.set(Calendar.MILLISECOND, 999);
        return clr;
    }
}

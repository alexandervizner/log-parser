package org.homebrewed.timing_report.parser;

import org.homebrewed.timing_report.matcher.Matcher;

import java.util.List;
import java.util.Scanner;

/**
 * Parser interface for parsing of input data
 */
public interface Parser {
    void addMatcher(Matcher matcher);

    List<Matcher> getMatchers();

    void parse(Scanner scanner);
}

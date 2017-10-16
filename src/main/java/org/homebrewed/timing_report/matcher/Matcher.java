package org.homebrewed.timing_report.matcher;

/**
 * Matcher interface that present particular atomic unit to parse.
 */
public interface Matcher<T> {
    boolean validate(String str);

    T getResult();

    String getName();
}

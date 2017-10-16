package org.homebrewed.timing_report.report;

import java.io.PrintStream;

/**
 * Reports interface for presentation of data
 */
public interface Report {
    void outputTo(PrintStream stream);
    Report calculate();
}

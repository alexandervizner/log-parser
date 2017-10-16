package org.homebrewed.timing_report.matcher;

import org.homebrewed.timing_report.util.Utils;

public class DurationMatcher implements Matcher<Integer> {

    private Integer duration;
    private Integer timeout;

    public DurationMatcher(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean validate(String str) {
        if (Utils.isEmpty(str)) {
            return false;
        }

        try {
            int v = Integer.parseInt(str.trim());
            if (v <= this.timeout) {
                this.duration = v;
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    @Override
    public Integer getResult() {
        return duration;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return this.duration.toString();
    }
}

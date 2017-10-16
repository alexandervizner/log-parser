package org.homebrewed.timing_report.matcher;

import org.homebrewed.timing_report.util.Utils;

import java.util.Arrays;

public class RequestMatcher implements Matcher<String> {

    private String requestString = "";
    private String[] keyWords;

    public RequestMatcher(String... keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public boolean validate(String str) {
        if (!Utils.isEmpty(str) && Arrays.stream(keyWords).anyMatch(str::startsWith)) {
            this.requestString = str;
            return true;
        }

        return false;
    }

    @Override
    public String getResult() {
        return null;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return requestString;
    }
}

package org.homebrewed.timing_report.matcher;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.homebrewed.timing_report.util.Constants.DEFAULT_DATE_FORMAT;


public class DateTimeMatcher implements Matcher<Date> {

    private Date date;
    private String pattern;

    public DateTimeMatcher(String dateTimePattern) {
        this.pattern = dateTimePattern;
    }

    public Date getDate() {
        return date;
    }

    public boolean validate(String str) {
        try {
            DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
            this.date = df.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public Date getResult() {
        return date;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat(pattern).format(this.date);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}

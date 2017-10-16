package org.homebrewed.timing_report.util;

public class Constants {
    public static final String APP_CONFIG_FILENAME = "application.properties";
    public static final String APP_TOP_MAX_PARAM_NAME = "app.top.max";
    public static final String APP_KEYWORDS_PARAM_NAME = "app.keywords";
    public static final String APP_TIMEOUT_PARAM_NAME = "app.timeout";
    public static final String APP_DATETIME_FORMAT_PARAM_NAME = "app.datetimeformat";
    public static final String APP_DEBUG_PARAM_NAME = "app.debug";

    public static final String DEFAULT_TOKENS_DELIMITER = " ";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss,SSS";
    public static final int DEFAULT_N_MAX = 100;
    public static final int DEFAULT_TIMEOUT = 5000;
    public static final String[] DEFAULT_KEYWORDS = new String[]{"contentId", "msisdn", "get", "update"};
    public static final boolean DEFAULT_DEBUG = false;
    public static final String DEFAULT_FILENAME = "";
}

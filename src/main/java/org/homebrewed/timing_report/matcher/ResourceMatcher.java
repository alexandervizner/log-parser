package org.homebrewed.timing_report.matcher;

import org.homebrewed.timing_report.util.Utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of Mather that intended to hide logic behind resource type determination
 */
public class ResourceMatcher implements Matcher<String> {

    private String[] keyWords;
    private String resource = "";

    public ResourceMatcher() {
    }

    public ResourceMatcher(String... keyWords) {
        this.keyWords = keyWords;
    }

    public String getResult() {
        return this.resource;
    }

    /**
     * Implements logic to determinate the passed string represents resource
     *
     * @param str resource string
     * @return boolean
     */
    @Override
    public boolean validate(String str) {

        if (Utils.isEmpty(str)) {
            return false;
        }

        // Assuming str is relative url
        if (str.startsWith("/")) {
            try {
                URL url = new URL("http://www.stub.com" + str);
                if (url.getQuery() != null) {
                    for (String word : keyWords) {
                        Map<String, String> params = getQueryParams(url.getQuery());
                        if (params.containsKey(word)) {
                            resource = str;
                            return true;
                        }
                    }
                }
            } catch (MalformedURLException ignored) {
                return false;
            }
        }

        // Or plain 'get/update/etc' resources
        if (str.chars().allMatch(Character::isLetter)) {
            for (String keyWord : keyWords) {
                if (str.startsWith(keyWord)) {
                    resource = str;
                    return true;
                }
            }
        }

        return false;
    }

    private Map<String, String> getQueryParams(String url) throws ArrayIndexOutOfBoundsException {
        Map<String, String> ret = new HashMap<>();

        for (String queryParam : url.split("&")) {
            String[] pair = queryParam.split("=");

            if (pair.length == 0) {
                continue;
            }

            String key = pair[0].trim();
            String value = "";

            if (pair.length == 2) {
                value = pair[1].trim();
            }

            ret.put(key, value);
        }

        return ret;
    }

    private String decode(String encoded) {
        try {
            return URLDecoder.decode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return this.resource;
    }
}

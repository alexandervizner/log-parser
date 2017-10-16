package org.homebrewed.timing_report.statistic;

import java.util.Date;

/**
 * Requests hourly statistic container
 */
public class RequestsHourlyStatistic{

    private int requestsCounter;

    private Date dateTime;

    public RequestsHourlyStatistic(){

    }

    public void increaseRequestCounter() {
        requestsCounter += 1;
    }

    public int getRequestsCounter() {
        return requestsCounter;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

}

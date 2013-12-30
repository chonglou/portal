package com.odong.core.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by flamen on 13-12-30上午2:29.
 */
public class TimeHelper {

    public Date plus(Date date, int second) {
        return new DateTime(date).plusSeconds(second).toDate();
    }

    public Date nextDay(int clock) {
        return new DateTime().plusDays(1).millisOfDay().withMinimumValue().withHourOfDay(clock).toDate();
    }

    public Date max() {
        return new DateTime().withYear(9999).dayOfYear().withMaximumValue().millisOfDay().withMaximumValue().toDate();
    }
}

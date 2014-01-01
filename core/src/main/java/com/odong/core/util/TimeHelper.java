package com.odong.core.util;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by flamen on 13-12-30上午2:29.
 */
@Component("core.util.time")
public class TimeHelper {

    public Date mouthMin(int year, int month) {
        return new DateTime().withYear(year).withMonthOfYear(month).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
    }

    public Date mouthMax(int year, int month) {
        return new DateTime().withYear(year).withMonthOfYear(month).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
    }

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

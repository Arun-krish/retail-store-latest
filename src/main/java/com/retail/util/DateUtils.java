package com.retail.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date fetchThreeMonthsBackDateFromCurrentDate() {
        Date referenceDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        c.add(Calendar.MONTH, -3);
        return c.getTime();
    }

    public static Date subractOneDayFromGivenDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }

    public static Date addOneDayFromGivenDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
}

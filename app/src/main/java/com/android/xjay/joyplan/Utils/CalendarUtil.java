package com.android.xjay.joyplan.Utils;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
    /**
     * 计算在 DATE1 与 DATE2之间的天数。
     */
    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算在 DATE1 与 DATE2之间的小时数。
     */
    public static int hoursBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_hours = (time2 - time1) / (1000 * 3600);

        return Integer.parseInt(String.valueOf(between_hours));
    }

}

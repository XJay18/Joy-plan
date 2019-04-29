package com.android.xjay.joyplan.Calendar;

import java.io.Serializable;

@SuppressWarnings("unused")
final class Month implements Serializable {
    private int diff;//日期偏移
    private int count;
    private int month;
    private int year;

    int getDiff() {
        return diff;
    }

    void setDiff(int diff) {
        this.diff = diff;
    }

    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    int getMonth() {
        return month;
    }

    void setMonth(int month) {
        this.month = month;
    }

    int getYear() {
        return year;
    }

    void setYear(int year) {
        this.year = year;
    }
}

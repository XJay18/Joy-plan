package com.android.xjay.joyplan;

public class Agenda {
    int index = -1;
    public String title;
    public String start_time;
    public String end_time;
    public String notation;
    public String address;
    public Agenda() {};
    public Agenda(String title, String start_time, String end_time, String content, String address) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.notation = notation;
        this.title = title;
        this.address = address;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStarttime() {
        return this.start_time;
    }

    public String getEndtime() {
        return this.end_time;
    }

    public String getNotation() {
        return this.notation;
    }

    public String getAddress() {
        return this.address;
    }
}

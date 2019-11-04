package com.android.xjay.joyplan;

public class Agenda {
    int index = -1;
    public String title;
    public String start_time;
    public String end_time;
    public String content;
    public String address;
    public Agenda() {};
    public Agenda(String title, String start_time, String end_time, String content, String address) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.content = content;
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

    public String getContent() {
        return this.content;
    }

    public String getAddress() {
        return this.address;
    }
}

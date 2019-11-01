package com.android.xjay.joyplan;

public class Agenda {
    int index = -1;
    String title;
    String start_time;
    String end_time;
    String notation;
    String address;

    public Agenda(String title, String start_time, String end_time, String notation, String address) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.notation = notation;
        this.title = title;
        this.address = address;
    }
}

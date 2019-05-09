package com.android.xjay.joyplan;

import com.android.xjay.calendarview.Calendar;

public class Agenda {
    String title;
    String start_time;
    String end_time;
    String content;
    String address;
    public Agenda(String title,String start_time,String end_time,String content,String address){
        this.start_time=start_time;
        this.end_time=end_time;
        this.content=content;
        this.title=title;
        this.address=address;
    }
}

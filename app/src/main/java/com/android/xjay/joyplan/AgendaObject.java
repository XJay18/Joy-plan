package com.android.xjay.joyplan;

public class AgendaObject {
    //private String userid;
    private String title;
    private String starttime;
    private String endtime;
    private String content;
    private String address;

    public AgendaObject() {};
    public AgendaObject(String t,String s,String e,String c,String a) {
        //this.userid=u;
        this.title=t;
        this.starttime=s;
        this.endtime=e;
        this.content=c;
        this.address=a;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStarttime() {
        return this.starttime;
    }

    public String getEndtime() {
        return this.endtime;
    }

    public String getContent() {
        return this.content;
    }

    public String getAddress() {
        return this.address;
    }

}



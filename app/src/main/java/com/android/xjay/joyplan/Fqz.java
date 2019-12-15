package com.android.xjay.joyplan;

public class Fqz {
    private String startoftime;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;

    public Fqz() {
        this.startoftime = "0";
        this.monday = 0;
        this.tuesday = 0;
        this.wednesday = 0;
        this.thursday = 0;
        this.friday = 0;
        this.saturday = 0;
        this.sunday = 0;
    }

    public Fqz(String start) {
        this.startoftime = start;
        this.monday = 0;
        this.tuesday = 0;
        this.wednesday = 0;
        this.thursday = 0;
        this.friday = 0;
        this.saturday = 0;
        this.sunday = 0;
    }

    public Fqz(String start, int m, int tues, int w, int th, int f, int s, int sun) {
        this.startoftime = start;
        this.monday = m;
        this.tuesday = tues;
        this.wednesday = w;
        this.thursday = th;
        this.friday = f;
        this.saturday = s;
        this.sunday = sun;
    }

    public Fqz(int m, int tues, int w, int th, int f, int s, int sun) {
        this.startoftime = "0";
        this.monday = m;
        this.tuesday = tues;
        this.wednesday = w;
        this.thursday = th;
        this.friday = f;
        this.saturday = s;
        this.sunday = sun;
    }

    public String getStartoftime() {
        return this.startoftime;
    }
    public int getMonday() {
        return this.monday;
    }
    public int getTuesday() {
        return this.tuesday;
    }
    public int getWednesday() {
        return this.wednesday;
    }
    public int getThursday() {
        return this.thursday;
    }
    public int getFriday() {
        return this.friday;
    }
    public int getSaturday() {
        return this.saturday;
    }
    public int getSunday() {
        return this.sunday;
    }
    public int getTotal(){
        return this.monday+this.tuesday+this.wednesday+this.thursday+this.friday+this.saturday+this.sunday;
    }
}

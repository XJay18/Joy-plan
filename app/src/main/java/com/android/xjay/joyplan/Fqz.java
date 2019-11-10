package com.android.xjay.joyplan;

public class Fqz {
    /**
     *
     */
    public String startoftime;
    public int monday;
    public int tuesday;
    public int wednesday;
    public int thursday;
    public int friday;
    public int saturday;
    public int sunday;

    public Fqz(String start){
        this.startoftime=start;
        this.monday=0;
        this.tuesday=0;
        this.wednesday=0;
        this.thursday=0;
        this.friday=0;
        this.saturday=0;
        this.sunday=0;
    };
    public Fqz(String start, int m, int tues, int w, int th, int f, int s, int sun){
        this.startoftime=start;
        this.monday=m;
        this.tuesday=tues;
        this.wednesday=w;
        this.thursday=th;
        this.friday=f;
        this.saturday=s;
        this.sunday=sun;
    }
    public Fqz(int m, int tues, int w, int th, int f, int s, int sun){
        this.startoftime="0";
        this.monday=m;
        this.tuesday=tues;
        this.wednesday=w;
        this.thursday=th;
        this.friday=f;
        this.saturday=s;
        this.sunday=sun;
    }
}

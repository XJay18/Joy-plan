package com.android.xjay.joyplan;

public class AgendaStatistics {
    private String theday;
    private int learning;
    private int sport;
    private int entertainment;
    private int others;

    public AgendaStatistics() {};
    public AgendaStatistics(String t,int l,int s,int e,int o) {
        this.theday=t;
        this.learning=l;
        this.sport=s;
        this.entertainment=e;
        this.others=o;
    }

    public String getTheday() {
        return this.theday;
    }

    public int getLearning() {
        return this.learning;
    }

    public int getSport() {
        return this.sport;
    }

    public int getEntertainment() {
        return this.entertainment;
    }

    public int getOthers() {
        return this.others;
    }
}


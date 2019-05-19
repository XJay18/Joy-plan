package com.android.xjay.joyplan;

public class Course {
    public String  dayofweek;
    public String courseName;
    public String startIndex;
    public String numOfCourse;

    public Course(String courseName,int dayofweek,int startIndex,int numOfCourse){
        this.courseName=courseName;
        this.dayofweek=new Integer(dayofweek).toString();
        this.startIndex=new Integer(startIndex).toString();
        this.numOfCourse=new Integer(numOfCourse).toString();
    }
}

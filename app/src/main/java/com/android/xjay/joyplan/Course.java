package com.android.xjay.joyplan;

public class Course {
    public int dayofweek;
    public String courseName;
    public int startIndex;
    public int numOfCourse;
    public int startWeek;
    public int endWeek;
    public String address;
    public String teacherName;

    public Course(String courseName,int dayofweek,int startWeek,int endWeek,int startIndex,int numOfCourse,String address,String teacherName){
        this.courseName=courseName;
        this.address=address;
        this.teacherName=teacherName;
        this.startWeek=startWeek;
        this.endWeek=endWeek;
        this.dayofweek=dayofweek;
        this.startIndex=startIndex;
        this.numOfCourse=numOfCourse;
    }
}

package com.android.xjay.joyplan;

public class Course {
    public int year;
    public int index0fSemester;
    public int dayofweek;
    public String courseName;
    public int startIndex;
    public int numOfCourse;
    public int startWeek;
    public int endWeek;
    public String address;
    public String teacherName;

    public Course(int year,int index0fSemester,String courseName,int dayofweek,int startWeek,int endWeek,int startIndex,int numOfCourse,String address,String teacherName){
        this.year=year;
        this.index0fSemester=index0fSemester;
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

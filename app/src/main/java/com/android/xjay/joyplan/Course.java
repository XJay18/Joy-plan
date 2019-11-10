package com.android.xjay.joyplan;

/**
 * 课程类
 * 一个课程包括 学年，学期，星期几，课程名，第几节，节数，开始周，结束周，地点，教师名，备注 等属性
 */
public class Course {
    private int year;
    private int index0fSemester;
    private int dayofweek;
    private String courseName;
    private int startIndex;
    private int numOfCourse;
    private int startWeek;
    private int endWeek;
    private String address;
    private String teacherName;
    private String notation;


    /**
     * @param year            学年
     * @param index0fSemester 学期
     * @param courseName      课程名
     * @param dayofweek       星期几
     * @param startWeek       开始周
     * @param endWeek         结束周
     * @param startIndex      开始节
     * @param numOfCourse     节数
     * @param address         地点
     * @param teacherName     教师名
     * @param notation        备注
     */
    public Course(int year, int index0fSemester, String courseName, int dayofweek, int startWeek, int endWeek, int startIndex, int numOfCourse, String address, String teacherName, String notation) {
        this.year = year;
        this.index0fSemester = index0fSemester;
        this.courseName = courseName;
        this.address = address;
        this.teacherName = teacherName;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.dayofweek = dayofweek;
        this.startIndex = startIndex;
        this.numOfCourse = numOfCourse;
        this.notation = notation;
    }

    /**
     * 不含备注的构造函数，默认备注为 “备注”
     *
     * @param year            学年
     * @param index0fSemester 学期
     * @param courseName      课程名
     * @param dayofweek       星期几
     * @param startWeek       开始周
     * @param endWeek         结束周
     * @param startIndex      开始节
     * @param numOfCourse     节数
     * @param address         地点
     * @param teacherName     教师名
     */
    public Course(int year, int index0fSemester, String courseName, int dayofweek, int startWeek, int endWeek, int startIndex, int numOfCourse, String address, String teacherName) {
        this.year = year;
        this.index0fSemester = index0fSemester;
        this.courseName = courseName;
        this.address = address;
        this.teacherName = teacherName;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.dayofweek = dayofweek;
        this.startIndex = startIndex;
        this.numOfCourse = numOfCourse;
        this.notation = "备注";
    }

    /**
     * 获取学年
     *
     * @return 学年
     */
    public int getYear() {
        return year;
    }

    /**
     * 获取学期
     *
     * @return 学期
     */
    public int getIndex0fSemester() {
        return index0fSemester;
    }

    /**
     * 获取星期几
     *
     * @return 星期几
     */
    public int getDayofweek() {
        return dayofweek;
    }


    /**
     * 获取结束周
     *
     * @return 结束周
     */
    public int getEndWeek() {
        return endWeek;
    }

    /**
     * 获取节数
     *
     * @return 节数
     */
    public int getNumOfCourse() {
        return numOfCourse;
    }

    /**
     * 获取开始节
     *
     * @return 开始节
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * 获取开始周
     *
     * @return 开始周
     */
    public int getStartWeek() {
        return startWeek;
    }

    /**
     * 获取地点
     *
     * @return 地点
     */
    public String getAddress() {
        return address;
    }


    /**
     * 获取课程名
     *
     * @return 课程名
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * 获取备注
     *
     * @return 备注
     */
    public String getNotation() {
        return notation;
    }

    /**
     * 获取教师名
     *
     * @return 教师名
     */
    public String getTeacherName() {
        return teacherName;
    }


}

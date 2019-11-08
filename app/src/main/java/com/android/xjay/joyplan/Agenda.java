package com.android.xjay.joyplan;

/**
 * 日程类
 * 一个日程包括 标题，开始时间，结束时间，备注，地点 等属性
 */
public class Agenda {

    /**
     * 标题
     */
    private String title;

    /**
     * 开始时间字符串
     */
    private String start_time;

    /**
     * 结束时间字符串
     */
    private String end_time;

    /**
     * 备注
     */
    private String notation;

    /**
     * 地点
     */
    private String address;

    public Agenda(String title, String start_time, String end_time, String notation, String address) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.notation = notation;
        this.title = title;
        this.address = address;
    }

    /**
     * 获取标题
     * @return 标题字符串
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * 获取开始时间
     * @return 开始时间字符串
     */
    public String getStarttime() {
        return this.start_time;
    }

    /**
     * 获取结束时间
     * @return 结束时间字符串
     */
    public String getEndtime() {
        return this.end_time;
    }

    /**
     * 获取备注
     * @return 结束时间字符串
     */
    public String getNotation() {
        return this.notation;
    }

    /**
     * 获取地点
     * @return 地点字符串
     */
    public String getAddress() {
        return this.address;
    }
}

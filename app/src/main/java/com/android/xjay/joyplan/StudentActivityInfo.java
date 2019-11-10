package com.android.xjay.joyplan;


/**
 * 学生活动信息
 */
public class StudentActivityInfo {
    private String title;
    private String info;
    private String starttime;
    private String endtime;
    private String address;
    byte[] img;

    /**
     * 构造函数
     *
     * @param Title     标题
     * @param Info      内容
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @param Address   地址
     * @param img       图片
     */
    public StudentActivityInfo(String Title, String Info, String starttime, String endtime, String Address, byte[] img) {
        title = Title;
        info = Info;
        this.starttime = starttime;
        this.endtime = endtime;
        address = Address;
        this.img = img;
    }

    /**
     * 构造函数
     *
     * @param Title     标题
     * @param Info      内容
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @param Address   地址
     */
    public StudentActivityInfo(String Title, String Info, String starttime, String endtime, String Address) {
        title = Title;
        info = Info;
        this.starttime = starttime;
        this.endtime = endtime;
        address = Address;
        img = null;
    }

    public String getAddress() {
        return address;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getInfo() {
        return info;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getImg() {
        return img;
    }

}

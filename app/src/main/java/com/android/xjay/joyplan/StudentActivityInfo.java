package com.android.xjay.joyplan;


public class StudentActivityInfo {
    private String title;
    private String info;
    private String starttime;
    private String endtime;
    private String address;
    byte[] img;

    public StudentActivityInfo(String Title, String Info, String starttime, String endtime, String Address,byte []img) {
        title = Title;
        info = Info;
        this.starttime = starttime;
        this.endtime = endtime;
        address = Address;
        this.img=img;
    }

    public StudentActivityInfo(String Title, String Info, String starttime, String endtime, String Address){
        title = Title;
        info = Info;
        this.starttime = starttime;
        this.endtime = endtime;
        address = Address;
        img=null;
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

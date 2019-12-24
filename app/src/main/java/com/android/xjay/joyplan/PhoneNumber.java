package com.android.xjay.joyplan;

public class PhoneNumber {
    private static String phone_number;
    private static PhoneNumber phoneNumber=new PhoneNumber();
    private PhoneNumber(){
    }
    public static PhoneNumber getInstance(){
        return phoneNumber;
    }


    public void setPhoneNumber(String num){
        phone_number=num;
    }
    public String getPhone_number(){
        return phone_number;
    }
}

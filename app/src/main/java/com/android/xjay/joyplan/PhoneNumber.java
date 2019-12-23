package com.android.xjay.joyplan;

public class PhoneNumber {
    private static String phone_number;
    PhoneNumber(String num){
        phone_number=num;
    }
    PhoneNumber(){}
    String getPhone_number(){
        return phone_number;
    }
}

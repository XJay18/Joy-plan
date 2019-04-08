package com.android.xjay.joyplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import cn.smssdk.SMSSDK;
import cn.smssdk.EventHandler;
public class PhoneActivity extends AppCompatActivity {
    private EditText et_phone;
    private EditText et_cord;
    private Button btn_phone_register;
    private Button btn_getcord;
    EventHandler eventHandler;
    private String phone_number;
    private String cord_number;
    private int time=60;
    private boolean flag = true;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_phone);
        getId();

        /*eventHandler=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /*
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    if(smart) {
                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",
                                Toast.LENGTH_LONG).show();
                        et_phone.requestFocus();
                        return;
                    }
                }
            }
            if(result==SMSSDK.RESULT_COMPLETE)
            {

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码输入正确",
                            Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                if(flag)
                {
                    btn_getcord.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    et_phone.requestFocus();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                }
            }

        }

    };
    /*
     *  获取ID
     */
    private void getId(){
        et_phone=findViewById(R.id.et_phonenumber);
        et_cord=findViewById(R.id.et_cord);
        btn_phone_register=findViewById(R.id.btn_phone_register);
        btn_getcord=findViewById(R.id.btn_getcord);
        btn_phone_register.setOnClickListener(new MyOnClickListener());
        btn_getcord.setOnClickListener(new MyOnClickListener());
    }
    /**
     * 按钮点击事件
     */
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_getcord:
                    if (judPhone())//去掉左右空格获取字符串
                    {
                        SMSSDK.getVerificationCode("86", phone_number);
                        et_cord.requestFocus();
                    }
                    break;
                case R.id.btn_phone_register:
                    /*if (judCord())
                        SMSSDK.submitVerificationCode("86", phone_number, cord_number);
                    flag = false;*/
                    //可删除
                    Intent intent=new Intent(PhoneActivity.this,Register.class);
                    if(et_phone.toString().length()>0){
                        intent.setClass(PhoneActivity.this,Register.class);
                        intent.putExtra("data",et_phone.getText().toString());
                    }
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
    private boolean judPhone()
    {
        if(TextUtils.isEmpty(et_phone.getText().toString().trim()))
        {
            Toast.makeText(PhoneActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            et_phone.requestFocus();
            return false;
        }
        else if(et_phone.getText().toString().trim().length()!=11)
        {
            Toast.makeText(PhoneActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            et_phone.requestFocus();
            return false;
        }
        else
        {
            phone_number=et_phone.getText().toString().trim();
            String num="[1][358]\\d{9}";
            if(phone_number.matches(num))
                return true;
            else
            {
                Toast.makeText(PhoneActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean judCord()
    {
        judPhone();
        if(TextUtils.isEmpty(et_cord.getText().toString().trim()))
        {
            Toast.makeText( PhoneActivity.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            et_cord.requestFocus();
            return false;
        }
        else if(et_cord.getText().toString().trim().length()!=4)
        {
            Toast.makeText(PhoneActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            et_cord.requestFocus();

            return false;
        }
        else
        {
            cord_number=et_cord.getText().toString().trim();
            return true;
        }

    }

}

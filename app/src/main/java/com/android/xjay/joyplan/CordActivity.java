package com.android.xjay.joyplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class CordActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 验证码输入框
     */
    private EditText et_cord;
    /**
     * 下一步按钮
     */
    private Button btn_next;
    /**
     * 返回按钮
     */
    private Button btn_return;
    /**
     * 电话号码
     */
    private String phone_number;
    /**
     * 验证码
     */
    private String cord_number;
    /**
     * 事件处理
     */
    private EventHandler eventHandler;
    /**
     * 验证内容是否合法
     */
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cord);
        MobSDK.init(this, "2a9ffe39f98ba", "37082c7739c2e2a900e3dd3fee4df879");
        phone_number = getIntent().getStringExtra("data0");
        SMSSDK.getVerificationCode("86", phone_number);
        System.out.println("问题" + phone_number);
        getId();
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean) data;
                    if (smart) {
                        Toast.makeText(getApplicationContext(), "该手机号已经注册过，请重新输入",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            if (result == SMSSDK.RESULT_COMPLETE) {

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码输入正确",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CordActivity.this, RegisterActivity.class);
                    intent.setClass(CordActivity.this, RegisterActivity.class);
                    intent.putExtra("data", phone_number);
                    startActivity(intent);
                }
            } else {
                if (flag) {
                    Toast.makeText(getApplicationContext(), "验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "验证码输入错误", Toast.LENGTH_LONG).show();
                }
            }

        }

    };

    /**
     * 获取id
     */
    private void getId() {
        et_cord = findViewById(R.id.et_cord);
        btn_next = findViewById(R.id.btn_cord_next);
        btn_return = findViewById(R.id.cord_return);
        btn_next.setOnClickListener(this);
        btn_return.setOnClickListener(this);
    }

    /**
     * 处理点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cord_next:

                if (judCord())
                    SMSSDK.submitVerificationCode("86", phone_number, cord_number);
                flag = false;

                break;
            case R.id.cord_return:
                Intent intent = new Intent(CordActivity.this, PhoneActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 输出提示信息
     */
    private boolean judCord() {
        if (TextUtils.isEmpty(et_cord.getText().toString().trim())) {
            Toast.makeText(CordActivity.this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_cord.getText().toString().trim().length() != 4) {
            Toast.makeText(CordActivity.this, "您的验证码位数不正确", Toast.LENGTH_LONG).show();
            return false;
        } else {
            cord_number = et_cord.getText().toString().trim();
            return true;
        }

    }
}
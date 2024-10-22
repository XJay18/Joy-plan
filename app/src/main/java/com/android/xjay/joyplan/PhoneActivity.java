package com.android.xjay.joyplan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.xjay.joyplan.web.WebServiceGet;

public class PhoneActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 电话号码输入框
     */
    private EditText et_phone;
    /**
     * 获取验证码按钮
     */
    private Button btn_phone_next;
    /**
     * 返回按钮
     */
    private Button btn_phone_return;
    /**
     * 储存电话号码
     */
    private String phone_number;
    /**
     * 判断内容是否合法
     */
    private Boolean legal;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_phone);
        getId();
    }

    /**
     * 绑定组件id
     */
    private void getId() {
        legal = false;
        et_phone = findViewById(R.id.et_phonenumber);
        btn_phone_next = findViewById(R.id.btn_phone_next);
        btn_phone_next.setOnClickListener(this);
        btn_phone_return = findViewById(R.id.phone_return);
        btn_phone_return.setOnClickListener(this);
    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone_next:
                legal = setphone();
                if (legal) {

                    //new Thread(new RegThread()).start();
                    Intent intent = new Intent(PhoneActivity.this, CordActivity.class);
                    intent.setClass(PhoneActivity.this, CordActivity.class);
                    intent.putExtra("data0", phone_number);
                    startActivity(intent);
                }

                break;
            case R.id.phone_return:
                Intent intent1 = new Intent(PhoneActivity.this, WelcomeActivity.class);
                startActivity(intent1);
        }
    }


    /**
     * 开线程调用http方法访问服务器并获得返回数据
     */
    private class RegThread implements Runnable {
        @Override
        public void run() {
            //获取服务器返回数据
            String PhoLet = WebServiceGet.phoneGet(phone_number);
            //更新UI，界面处理
            showReq(PhoLet);
        }
    }

    /**
     * 用服务器返回的数据进行页面更新
     */
    private void showReq(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response.equals("no_connection")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            PhoneActivity.this);
                    builder.setTitle("登陆信息");
                    builder.setMessage("未连接至服务器，无法注册");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //TODO 登录失败后的页面跳转
                                }
                            });
                    builder.show();
                } else if (response.equals("false")) {
                    Intent intent = new Intent(PhoneActivity.this, CordActivity.class);
                    intent.setClass(PhoneActivity.this, CordActivity.class);
                    intent.putExtra("data0", phone_number);
                    startActivity(intent);
                } else {
                    //TODO 此处为数据库中含有该电话号码时的时候
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            PhoneActivity.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("手机号已存在");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //TODO 登录失败后的页面跳转
                                }
                            });
                    builder.show();
                }
            }
        });
    }

    /**
     * 判断输入的电话号码是否合法
     */
    private Boolean setphone() {
        phone_number = et_phone.getText().toString().trim();
        if (phone_number.length() <= 0) {
            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (phone_number.length() < 11) {
            Toast.makeText(this, "手机号码位数不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

package com.android.xjay.joyplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.xjay.joyplan.web.WebServicePost;

public class PhoneActivity extends AppCompatActivity {
    private EditText et_phone;
    private Button btn_phone_next;
    private Button btn_phone_return;
    private String phone_number;
    ProgressDialog dialog;
    private Boolean legal;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_phone);
        getId();
    }

    /*
     *  获取ID
     */
    private void getId() {
        legal = false;
        et_phone = findViewById(R.id.et_phonenumber);
        btn_phone_next = findViewById(R.id.btn_phone_next);
        btn_phone_next.setOnClickListener(new MyOnClickListener());
        btn_phone_return = findViewById(R.id.phone_return);
        btn_phone_return.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 按钮点击事件
     */
    class MyOnClickListener implements View.OnClickListener {
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
    }

    private class RegThread implements Runnable {
        @Override
        public void run() {
            //获取服务器返回数据
            String RegRet = WebServicePost.checkphonePost(phone_number, "PhoLet");
            //更新UI，界面处理
            showReq(RegRet);
        }
    }

    private void showReq(final String RegRet) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (RegRet.equals("false")) {
                    Intent intent = new Intent(PhoneActivity.this, CordActivity.class);
                    intent.setClass(PhoneActivity.this, CordActivity.class);
                    intent.putExtra("data0", phone_number);
                    startActivity(intent);
                } else {
                    //TODO 此处为数据库中含有该电话号码时的时候

                }
            }
        });
    }

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

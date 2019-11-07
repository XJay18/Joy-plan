package com.android.xjay.joyplan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 登录按钮
     */
    private Button login;
    /**
     * 注册按钮
     */
    private Button register;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_welcome);
        getID();
    }

    /**
     * 绑定组件id
     */
    private void getID() {
        login = findViewById(R.id.btn_wel_login);
        register = findViewById(R.id.btn_wel_register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wel_login:
                Intent intent1 = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_wel_register:
//                    AlertDialog.Builder builder = new AlertDialog.Builder(
//                            WelcomeActivity.this);
//                    builder.setTitle("注册信息");
//                    builder.setMessage("注册功能暂时关闭");
//                    builder.setCancelable(true);
//
//                    builder.show();
                Intent intent2 = new Intent(WelcomeActivity.this, PhoneActivity.class);
                startActivity(intent2);
                break;
            }
        }
}

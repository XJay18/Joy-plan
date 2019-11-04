package com.android.xjay.joyplan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.xjay.joyplan.Notification.NotificationReceiver;
import com.android.xjay.joyplan.web.WebServiceGet;
import com.android.xjay.joyplan.web.WebServicePost;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    private Button btn_return;
    private NotificationReceiver notificationReceiver;
    //提示框
    private ProgressDialog dialog;
    //服务器返回的数据
    private String infoString;
    //判断输入内容的合法性
    private boolean legal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getID();
        notificationReceiver = new NotificationReceiver();
        regReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册notificationReceiver
        unregisterReceiver(notificationReceiver);
    }

    private void getID() {
        btn_login = findViewById(R.id.btn_login);
        btn_return = findViewById(R.id.main_return);
        btn_login.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        et_account.addTextChangedListener(new JumpTextWatcher(et_account, et_password));
    }

    @Override
    public void onClick(View v) {
//        switch(v.getId()){
//            case R.id.btn_login:
//                legal=setUser();
//                if(legal){
//                    dialog = new ProgressDialog(MainActivity.this);
//                    dialog.setTitle("正在登陆");
//                    dialog.setMessage("请稍后");
//                    dialog.setCancelable(true);//设置可以通过back键取消
//                    dialog.show();
//                    new Thread(new MyThread()).start();
//                }
//                break;
//            case R.id.main_return:
//                Intent intent=new Intent(MainActivity.this,WelcomeActivity.class);
//                startActivity(intent);
//                break;
//        }
        if (v.getId() == R.id.btn_login) {
            Toast.makeText(
                    MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(
                    MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.main_return) {
            Intent intent = new Intent(
                    MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
    }

    private class MyThread implements Runnable {
        @Override
        public void run() {
            //获取服务器返回的数据
            infoString = WebServiceGet.loginGet(et_account.getText().toString(),et_password.getText().toString());
            //infoString=WebServiceGet.selectagendaGet(et_account.getText().toString(),"SelAgen");
            //更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }

    /*
     * 跳转页面
     */
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            //更新UI
            @Override
            public void run() {
                if (response.equals("false")) {
                    //TODO 登陆失败后的操作
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            MainActivity.this);
                    builder.setTitle("登陆信息");
                    builder.setMessage("登陆失败");
                    //builder.setMessage("登录帐号或密码错误");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //TODO 登录失败后的页面跳转
                        }
                    });
                    builder.show();
                } else {
                    Intent intent = new Intent(MainActivity.this,
                            HomeActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
    }

    /*
     * 实现换行功能
     */
    private boolean setUser() {
        if (et_account.getText().toString().length() <= 0) {
            Toast.makeText(this, "帐号不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (et_password.getText().toString().length() <= 0) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private class JumpTextWatcher implements TextWatcher {
        private EditText mThisView;
        private View mNextView;

        public JumpTextWatcher(EditText vThis, View vNext) {
            super();
            mThisView = vThis;
            if (vNext != null) {
                mNextView = vNext;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if (str.contains("\r") || str.contains("\n")) {
                mThisView.setText(str.replace(
                        "\r", " ").replace("\n", ""));
                if (mNextView != null) {
                    mNextView.requestFocus();
                    if (mNextView instanceof EditText) {
                        EditText et = (EditText) mNextView;
                        et.setSelection(et.getText().length());
                    }
                }
            }
        }
    }

    private void regReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.notification");
        registerReceiver(notificationReceiver, intentFilter);
    }

}

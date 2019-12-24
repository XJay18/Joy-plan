package com.android.xjay.joyplan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.xjay.joyplan.Notification.NotificationReceiver;
import com.android.xjay.joyplan.Utils.JumpTextWatcher;
import com.android.xjay.joyplan.web.WebServiceGet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 帐号输入框
     */
    private EditText et_account;
    /**
     * 密码输入框
     */
    private EditText et_password;
    /**
     * 登录按钮
     */
    private Button btn_login;
    /**
     * 返回按钮
     */
    private Button btn_return;
    /**
     * 通知栏
     */
    private NotificationReceiver notificationReceiver;
    /**
     * 提示框
     */
    private ProgressDialog dialog;
    /**
     * 服务器返回的数据
     */
    private String infoString;
    /**
     * 判断内容的合法性
     */
    private boolean legal;
    /**
     * 储存电话号码
     */
    private String phone_number;
    /**
     * 储存密码
     */
    private String password;
    /**
     *  创建对象储存用户电话号码
     */
    private PhoneNumber PN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    /**
     * 绑定组件id
     */
    private void getID() {
        btn_login = findViewById(R.id.btn_login);
        btn_return = findViewById(R.id.main_return);
        btn_login.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        et_account.addTextChangedListener(
                new JumpTextWatcher(this, et_account, et_password));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                legal = setUser();
                if (legal) {
                    phone_number = et_account.getText().toString();
                    password = et_password.getText().toString();
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("正在登陆");
                    dialog.setMessage("请稍后");
                    dialog.setCancelable(true);//设置可以通过back键取消
                    dialog.show();
                    new Thread(new MyThread()).start();
                }
                break;
            case R.id.main_return:
                Intent intent = new Intent(
                        MainActivity.this,
                        WelcomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 开线程调用http方法访问服务器并获得返回数据
     */
    private class MyThread implements Runnable {
        @Override
        public void run() {
            // 获取服务器返回的数据
            infoString = WebServiceGet.loginGet(phone_number, password);
            // 更新UI，使用runOnUiThread()方法
            showResponse(infoString);
        }
    }

    /**
     * 用服务器返回的数据进行页面更新
     */
    private void showResponse(final String response) {

        runOnUiThread(new Runnable() {
            // 更新UI
            @Override
            public void run() {
                runHelper(response);
            }
        });
    }

    /**
     * 更新UI的辅助方法
     */
    private void runHelper(String response) {
        System.out.println("loginresponse="+response);
        if (response.equals("no_connection")) {
            dialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setTitle("登陆信息");
            builder.setMessage("未连接至服务器，免登录");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //TODO 登录失败后的页面跳转
                    Intent intent = new Intent(MainActivity.this,
                            HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_name", et_account.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            builder.show();
        } else if (response.equals("false")) {
            //TODO 登陆失败后的操作
            dialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setTitle("登陆信息");
            builder.setMessage("登陆失败");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //TODO 登录失败后的页面跳转
                }
            });
            builder.show();
        } else {
            PN=PhoneNumber.getInstance();
            PN.setPhoneNumber(phone_number);
            Intent intent = new Intent(MainActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            dialog.dismiss();
        }
    }

    /**
     * 限制输入内容
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

    /**
     * 注册notification
     */
    private void regReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.notification");
        registerReceiver(notificationReceiver, intentFilter);
    }

}

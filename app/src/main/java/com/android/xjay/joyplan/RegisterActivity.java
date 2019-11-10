package com.android.xjay.joyplan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.xjay.joyplan.web.WebServiceGet;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 用户名输入框
     */
    private EditText et_nickname;
    /**
     * 密码输入框
     */
    private EditText et_password;
    /**
     * 第二次密码输入框
     */
    private EditText et_password2;
    /**
     * 学校下拉框
     */
    private Spinner spin_university;
    /**
     * 返回按钮
     */
    private Button btn_return;
    /**
     * 确认按钮
     */
    private Button btn_submit;
    /**
     * 储存电话号码
     */
    private String phone_number;
    /**
     * 储存密码
     */
    private String password;
    /**
     * 储存昵称
     */
    private String nick_name;
    /**
     * 储存所选择的学校
     */
    private String university;
    /**
     * 提示框
     */
    private ProgressDialog dialog;
    /**
     * 判断内容合法性
     */
    private boolean legal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getID();
    }

    /**
     * 绑定组件id
     */
    private void getID() {
        legal = false;
        btn_submit = findViewById(R.id.btn_submit);
        btn_return = findViewById(R.id.register_return);
        et_nickname = findViewById(R.id.regi_nick_name);
        et_password = findViewById(R.id.regi_password);
        et_password2 = findViewById(R.id.regi_password_confirm);
        spin_university = findViewById(R.id.sp_university);
        phone_number = getIntent().getStringExtra("data");
        btn_submit.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        spin_university.setOnItemSelectedListener(new MyOnItemSelected());
        et_nickname.addTextChangedListener(new JumpTextWatcher(et_nickname, et_password));
        et_password.addTextChangedListener(new JumpTextWatcher(et_password, et_password2));
    }

    @Override
    public void onClick(View v) {
        //setUser();
        switch (v.getId()) {
            case R.id.btn_submit:
                legal = setUser();
                if (legal) {
                    password = et_password.getText().toString();
                    nick_name = et_nickname.getText().toString();
                    dialog = new ProgressDialog(RegisterActivity.this);
                    dialog.setTitle("正在注册");
                    dialog.setMessage("请稍后");
                    dialog.show();
                    new Thread(new RegThread()).start();
                }
                break;
            case R.id.register_return:
                Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 载入学校下拉框
     */
    class MyOnItemSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.sp_university:
                    university = parent.getItemAtPosition(position).toString();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /**
     * 开线程与服务器进行http通信
     */
    private class RegThread implements Runnable {
        @Override
        public void run() {
            //获取服务器返回数据
            String RegRet = WebServiceGet.registerGet(phone_number, password, nick_name, university);
            //更新UI，界面处理
            showReq(RegRet);
        }
    }

    /**
     * 获取服务器返回数据后更新页面
     */
    private void showReq(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response.equals("no_connection")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            RegisterActivity.this);
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
                } else if (response.equals("true")) {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册成功");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /*
                         *注册成功后的页面跳转
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                } else {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册失败");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /*
                         *注册失败后的页面跳转
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            /*Intent intent = new Intent(RegisterActivity.this,WelcomeActivity.class);
                            startActivity(intent);*/
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    /**
     * 对输入注册信息进行判断
     */
    private boolean setUser() {
        if (et_nickname.getText().toString().length() <= 0) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (et_password.getText().toString().length() <= 0 || et_password2.getText().toString().length() <= 0) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!et_password.getText().toString().equals(et_password2.getText().toString())) {
            Toast.makeText(this, "两次输入的密码不同", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * 实现换行功能
     */
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
                mThisView.setText(str.replace("\r", " ").replace("\n", ""));
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
}

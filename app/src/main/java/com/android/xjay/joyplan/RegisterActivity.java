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

import com.android.xjay.joyplan.web.WebServicePost;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText userpassword;
    private EditText userpassword2;
    private Spinner spin_university;
    private Button btn_return;
    private Button btn_submit;
    String phone_number;
    String university;
    ProgressDialog dialog;
    boolean legal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getID();
    }

    private void getID() {
        legal = false;
        btn_submit = findViewById(R.id.btn_submit);
        btn_return = findViewById(R.id.register_return);
        username = findViewById(R.id.regi_nick_name);
        userpassword = findViewById(R.id.regi_password);
        userpassword2 = findViewById(R.id.regi_password_confirm);
        spin_university = findViewById(R.id.sp_university);
        phone_number = getIntent().getStringExtra("data");
        btn_submit.setOnClickListener(this);
        spin_university.setOnItemSelectedListener(new MyOnItemSelected());
        username.addTextChangedListener(new JumpTextWatcher(username, userpassword));
        userpassword.addTextChangedListener(new JumpTextWatcher(userpassword, userpassword2));
    }

    @Override
    public void onClick(View v) {
        //setUser();
        switch (v.getId()) {
            case R.id.btn_submit:
                legal = setUser();
                if (legal) {
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

    private class RegThread implements Runnable {
        @Override
        public void run() {
            //获取服务器返回数据
            String RegRet = WebServicePost.registerPost(phone_number, userpassword.getText().toString(), username.getText().toString(), university, "RegLet");
            //更新UI，界面处理
            showReq(RegRet);
        }
    }

    private void showReq(final String RegRet) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (RegRet.equals("true")) {
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

    /*
     *对输入注册信息进行判断
     */
    private boolean setUser() {
        if (username.getText().toString().length() <= 0) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (userpassword.getText().toString().length() <= 0 || userpassword2.getText().toString().length() <= 0) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!userpassword.getText().toString().equals(userpassword2.getText().toString())) {
            Toast.makeText(this, "两次输入的密码不同", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /*
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

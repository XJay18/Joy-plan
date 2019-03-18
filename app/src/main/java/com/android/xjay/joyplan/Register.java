package com.android.xjay.joyplan;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.*;
import android.content.Intent;

public class Register extends AppCompatActivity {
    EditText username;
    EditText useraccount;
    EditText userpassword;
    EditText userpassword2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btn_submit=findViewById(R.id.btn_submit);
        username=(EditText)findViewById(R.id.regi_username);
        useraccount=(EditText)findViewById(R.id.regi_account);
        userpassword=(EditText)findViewById(R.id.regi_password);
        userpassword2=(EditText)findViewById(R.id.regi_password2);
        btn_submit.setOnClickListener(new MyOnClickListener());
        username.addTextChangedListener(new JumpTextWatcher(username,useraccount));
        useraccount.addTextChangedListener(new JumpTextWatcher(useraccount,userpassword));
        userpassword.addTextChangedListener(new JumpTextWatcher(userpassword,userpassword2));
    }
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            setUser();
        }
    }
    private void setUser(){
        UserDBHelper database=new UserDBHelper(Register.this,"LoginInfo",null,1);


        if(username.getText().toString().length()<=0||/*useraccount.getText().toString().length()<=0||*/userpassword.getText().toString().length()<=0||userpassword2.getText().toString().length()<=0)
        {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(username.getText().toString().length()>0)
        {
            String sql="select * from user where userid=?";
            Cursor cursor=database.getWritableDatabase().rawQuery(sql, new String[]{username.getText().toString()});
            if(cursor.moveToFirst())
            {
                Toast.makeText(this, "用户名已经存在", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(!userpassword.getText().toString().equals(userpassword2.getText().toString()))
        {
            Toast.makeText(this, "两次输入的密码不同", Toast.LENGTH_LONG).show();
            return;
        }
        if(database.AddUser(username.getText().toString(), userpassword.getText().toString()))
        {
            Toast.makeText(this, "用户注册成功", Toast.LENGTH_LONG).show();
            Intent intent=new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "用户注册失败", Toast.LENGTH_LONG).show();
        }
        database.close();
    }
    private class JumpTextWatcher implements TextWatcher {
        private EditText mThisView;
        private View mNextView;
        public JumpTextWatcher(EditText vThis,View vNext)
        {
            super();
            mThisView=vThis;
            if(vNext!=null){
                mNextView=vNext;
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s,int start,int count,int after){}
        @Override
        public void onTextChanged(CharSequence s, int start,int before,int count){}
        @Override
        public void afterTextChanged(Editable s){
            String str=s.toString();
            if(str.contains("\r")||str.contains("\n")){
                mThisView.setText(str.replace("\r"," ").replace("\n",""));
                if(mNextView!=null){
                    mNextView.requestFocus();
                    if(mNextView instanceof EditText){
                        EditText et=(EditText)mNextView;
                        et.setSelection(et.getText().length());
                    }
                }
            }
        }
    }
}

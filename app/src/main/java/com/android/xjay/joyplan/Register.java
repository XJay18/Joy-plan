package com.android.xjay.joyplan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.*;
import android.content.Intent;

import com.android.xjay.joyplan.web.WebServiceGet;
import com.android.xjay.joyplan.web.WebServicePost;

public class Register extends AppCompatActivity {
    private EditText username;
    private EditText userpassword;
    private EditText userpassword2;
    private Spinner spin_university;
    String phone_number;
    String university;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getID();
    }
    private void getID(){
        Button btn_submit=findViewById(R.id.btn_submit);
        username=(EditText)findViewById(R.id.regi_nick_name);
        userpassword=(EditText)findViewById(R.id.regi_password);
        userpassword2=(EditText)findViewById(R.id.regi_password2);
        spin_university=(Spinner)findViewById(R.id.sp_university);
        phone_number=getIntent().getStringExtra("data");
        btn_submit.setOnClickListener(new MyOnClickListener());
        spin_university.setOnItemSelectedListener(new MyOnItemSelected());
        username.addTextChangedListener(new JumpTextWatcher(username,userpassword));
        userpassword.addTextChangedListener(new JumpTextWatcher(userpassword,userpassword2));
    }
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //setUser();
            switch (v.getId()) {
                case R.id.btn_submit:
                    dialog = new ProgressDialog(Register.this);
                    dialog.setTitle("正在注册");
                    dialog.setMessage("请稍后");
                    dialog.show();
                    new Thread(new RegThread()).start();
            }
        }
    }
    class MyOnItemSelected implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
            switch(parent.getId()){
                case R.id.sp_university:
                    university=parent.getItemAtPosition(position).toString();
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public class RegThread implements Runnable{
        @Override
        public void run() {
            //获取服务器返回数据
           // String RegRet = WebServiceGet.registerHttpGet(phone_number,userpassword.getText().toString(),username.getText().toString(),university,"RegLet");
            String RegRet = WebServicePost.registerPost(phone_number,userpassword.getText().toString(),username.getText().toString(),university,"RegLet");
            //更新UI，界面处理
            showReq(RegRet);
        }
    }
    private void showReq(final String RegRet){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(RegRet.equals("true")){
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册成功");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Register.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }else{
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("注册信息");
                    builder.setMessage("注册失败");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            /*Intent intent = new Intent(Register.this,WelcomeActivity.class);
                            startActivity(intent);*/
                        }
                    });
                    builder.show();
                }
            }
        });
    }









    /*private void setUser(){
        UserDBHelper database=new UserDBHelper(Register.this,"LoginInfo",null,1);


        if(username.getText().toString().length()<=0||useraccount.getText().toString().length()<=0||userpassword.getText().toString().length()<=0||userpassword2.getText().toString().length()<=0)
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
    }*/
    /*
     * 实现换行功能
     */
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

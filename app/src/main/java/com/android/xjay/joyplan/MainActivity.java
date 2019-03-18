package com.android.xjay.joyplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.*;
import android.content.Intent;
/*
    the main activity for login

 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new MyOnClickListener());
        Button btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new MyOnClickListener());
        EditText et_account=(EditText)findViewById(R.id.et_account);
        EditText et_password=(EditText)findViewById(R.id.et_password);
        et_account.addTextChangedListener(new JumpTextWatcher(et_account,et_password));
    }

    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if(v.getId()==R.id.btn_login){
                Toast.makeText(MainActivity.this,"登陆成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }
            if(v.getId()==R.id.btn_register){
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        }
    }
    private class JumpTextWatcher implements TextWatcher{
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

package com.android.xjay.joyplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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

}

package com.android.xjay.joyplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    @Override
    protected void onCreate(Bundle saveInstanceState){ ;
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_welcome);
        getID();
    }
    private void getID(){
        login=findViewById(R.id.btn_wel_login);
        register=findViewById(R.id.btn_wel_register);
        login.setOnClickListener(new MyOnClickListener());
        register.setOnClickListener(new MyOnClickListener());
    }
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btn_wel_login:
                    Intent intent1=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btn_wel_register:
                    Intent intent2=new Intent(WelcomeActivity.this,PhoneActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    }
}

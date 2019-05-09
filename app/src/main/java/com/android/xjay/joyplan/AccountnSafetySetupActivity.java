package com.android.xjay.joyplan;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class AccountnSafetySetupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_accountnsafety);
        setListener();
    }

    private void setListener(){
        findViewById(R.id.ll_setup_accountnsafety_fc1).setOnClickListener(this);
        findViewById(R.id.ll_setup_accountnsafety_fc2).setOnClickListener(this);
        findViewById(R.id.ll_setup_accountnsafety_fc3).setOnClickListener(this);
        findViewById(R.id.bt_setup_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_setup_accountnsafety_fc1:
                Toast.makeText(this,"修改密码",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_setup_accountnsafety_fc2:
                Toast.makeText(this,"权限设置",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_setup_accountnsafety_fc3:
                Toast.makeText(this,"应用锁",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_setup_back:
                this.finish();
            default:break;
        }
    }

}

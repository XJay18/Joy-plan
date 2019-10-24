package com.android.xjay.joyplan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class AddfriendSetupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_addfriend);
        setListener();
    }

    private void setListener() {
        findViewById(R.id.ll_setup_addfriend_fc1).setOnClickListener(this);
        findViewById(R.id.ll_setup_addfriend_fc2).setOnClickListener(this);
        findViewById(R.id.ll_setup_addfriend_fc3).setOnClickListener(this);
        findViewById(R.id.bt_setup_addfriend_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setup_addfriend_fc1:
                Toast.makeText(this, "账号添加", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_setup_addfriend_fc2:
                Toast.makeText(this, "二维码添加", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_setup_addfriend_fc3:
                Toast.makeText(this, "我的二维码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_setup_addfriend_back:
                this.finish();
            default:
                break;
        }
    }
}

package com.android.xjay.joyplan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AccountnSafetySetupActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_accountnsafety);
        ((TextView) findViewById(R.id.tv_safety_user_name)).
                setText(getIntent().getStringExtra("user_name"));
        setListener();
    }

    private void setListener() {
        findViewById(R.id.ll_setup_accountnsafety_fc1).setOnClickListener(this);
        // Not Implemented.
        // findViewById(R.id.ll_setup_accountnsafety_fc2).setOnClickListener(this);
        findViewById(R.id.bt_setup_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setup_accountnsafety_fc1:
                Toast.makeText(this, "修改密码", Toast.LENGTH_SHORT).show();
                break;
            // Not Implemented.
            // case R.id.ll_setup_accountnsafety_fc2:
            //     Toast.makeText(this, "权限设置", Toast.LENGTH_SHORT).show();
            //     break;
            case R.id.bt_setup_back:
                this.finish();
            default:
                break;
        }
    }

}

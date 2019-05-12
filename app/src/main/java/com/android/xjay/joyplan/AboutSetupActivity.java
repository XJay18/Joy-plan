package com.android.xjay.joyplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.xjay.joyplan.Notification.NotificationTool;

public class AboutSetupActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext = AboutSetupActivity.this;
    LinearLayout linearLayout;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_about);
        linearLayout = (LinearLayout) findViewById(R.id.ll_setup_about_other);
        button = (Button) findViewById(R.id.bt_setup_about_back);
        setListener();
    }

    private void setListener() {
//        linearLayout.setOnClickListener(this);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setup_about_back:
                finish();
                break;
        }
    }
}

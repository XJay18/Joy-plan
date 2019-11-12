package com.android.xjay.joyplan;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class AboutSetupActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext = AboutSetupActivity.this;
    LinearLayout linearLayout;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_about);
        linearLayout = findViewById(R.id.ll_setup_about_other);
        button = findViewById(R.id.bt_setup_about_back);
        setListener();
    }

    private void setListener() {
//        linearLayout.setOnClickListener(this);
        button.setOnClickListener(this);

        findViewById(R.id.bt_setup_about_back).setOnClickListener(this);
        findViewById(R.id.ll_setup_about_other).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setup_about_back:
                finish();
                break;
            case R.id.ll_setup_about_other: {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle("Joyplan Version 1.0.1");
                mBuilder.setMessage(R.string.info_about);
                AlertDialog mAlert = mBuilder.create();
                mAlert.show();
            }
        }
    }
}

package com.android.xjay.joyplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

import javax.xml.transform.Templates;

public class HelpnfeedbackSetupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText tv_feedback;
    EditText tv_mailaddress;
    Button bt_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_helpnfeedback);
        tv_feedback=(EditText)findViewById(R.id.et_setup_feedback);
        tv_mailaddress=(EditText)findViewById(R.id.et_setup_mailaddress);
        bt_back=(Button)findViewById(R.id.bt_setup_helpnfeedback_back);
        setListener();
    }

    private void setListener(){
        bt_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_setup_helpnfeedback_back:
                finish();
                break;
            default:
                    break;

        }
    }
}

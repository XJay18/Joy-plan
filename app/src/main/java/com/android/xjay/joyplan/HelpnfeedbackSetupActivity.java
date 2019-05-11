package com.android.xjay.joyplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import javax.xml.transform.Templates;

public class HelpnfeedbackSetupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText tv_feedback;
    private EditText tv_mailaddress;
    private Button bt_back;
    private Button bt_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_helpnfeedback);
        tv_feedback=(EditText)findViewById(R.id.et_setup_feedback);
        tv_mailaddress=(EditText)findViewById(R.id.et_setup_mailaddress);
        bt_back=(Button)findViewById(R.id.bt_setup_helpnfeedback_back);
        bt_confirm=(Button)findViewById(R.id.btn_setup_confirm);
        setListener();
    }

    private void setListener(){
        bt_back.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_setup_helpnfeedback_back:{
                finish();
                break;
            }
            case R.id.btn_setup_confirm:{
                Toast.makeText(this,"您的反馈已经提交，谢谢您！",Toast.LENGTH_SHORT).show();
            }
            default:
                break;

        }
    }
}

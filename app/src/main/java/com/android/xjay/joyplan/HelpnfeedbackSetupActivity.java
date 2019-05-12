package com.android.xjay.joyplan;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.w3c.dom.Text;

import javax.xml.transform.Templates;

public class HelpnfeedbackSetupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_feedback;
    private EditText et_mailaddress;
    private Button bt_back;
    private Button bt_confirm;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_helpnfeedback);
        bt_back = (Button) findViewById(R.id.bt_setup_helpnfeedback_back);
        bt_confirm = (Button) findViewById(R.id.btn_setup_confirm);
        bt_confirm.setFocusable(true);
        bt_confirm.setFocusableInTouchMode(true);
        setListener();
        et_feedback=findViewById(R.id.et_setup_feedback);
        et_mailaddress=findViewById(R.id.et_setup_mailaddress);
        et_mailaddress.addTextChangedListener(new JumpTextWatcher(et_mailaddress,et_feedback));
        et_feedback.addTextChangedListener(new JumpTextWatcher(et_feedback,bt_confirm));
        animationView = findViewById(R.id.setup_anim_okay_blue);
    }

    private void setListener() {
        bt_back.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setup_helpnfeedback_back: {
                finish();
                break;
            }
            case R.id.btn_setup_confirm: {
                animationView.setAnimation("okay_blue.json");
                animationView.loop(false);
                Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        animationView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                };
                animationView.addAnimatorListener(mAnimationListener);
                animationView.playAnimation();
                Toast.makeText(this, "您的反馈已经提交，谢谢您！", Toast.LENGTH_SHORT).show();
            }
            default:
                break;

        }
    }

    private class JumpTextWatcher implements TextWatcher {

        private EditText mThisView = null;
        private View mNextView = null;

        public JumpTextWatcher(EditText vThis, View vNext) {
            super();
            mThisView = vThis;
            if (vNext != null) {
                mNextView = vNext;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            //发现输入回车符或换行符
            if (str.indexOf("\r") >= 0 || str.indexOf("\n") >= 0) {
                //去掉回车符和换行符
                mThisView.setText(str.replace("\r", "").replace("\n", ""));
                if (mNextView != null) {
                    //让下一个视图获得焦点，即将光标移到下个视图
                    mNextView.requestFocus();
                    if (mNextView instanceof EditText) {
                        EditText et = (EditText) mNextView;
                        //让光标自动移到编辑框内部的文本末尾
                        //方式一：直接调用EditText的setSelection方法
                        et.setSelection(et.getText().length());
                        //方式二：调用Selection类的setSelection方法
                        //Editable edit = et.getText();
                        //Selection.setSelection(edit, edit.length());
                    } else {
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mThisView.getWindowToken(), 0);
                    }
                }
            }
        }
    }
}

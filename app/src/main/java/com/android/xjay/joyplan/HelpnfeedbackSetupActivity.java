package com.android.xjay.joyplan;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.xjay.joyplan.Utils.JumpTextWatcher;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        et_feedback = findViewById(R.id.et_setup_feedback);
        et_mailaddress = findViewById(R.id.et_setup_mailaddress);
        et_mailaddress.addTextChangedListener(new JumpTextWatcher(this, et_mailaddress, et_feedback));
        et_feedback.addTextChangedListener(new JumpTextWatcher(this, et_feedback, bt_confirm));
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
                // TODO
                // CHECK HAS INPUT WHY ALWAYS TRUE?
                if (checkHasInput()) {
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
//                    Log.v("email", et_mailaddress.getText().toString());
//                    Log.v("feedback", et_feedback.getText().toString());
                    Toast.makeText(this, "您的反馈已经提交，谢谢您！", Toast.LENGTH_SHORT).show();
                }
            }
            default:
                break;
        }
    }

    private boolean checkHasInput() {
        Log.v("email", et_mailaddress.getText().toString());
        Log.v("feedback", et_feedback.getText().toString());
        Pattern p=Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$" );
        Matcher matcher = p.matcher(et_mailaddress.getText().toString());
        if (!matcher.find()) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("请输入邮箱地址");
            mBuilder.setMessage("您好，请填写您的邮箱地址以便我们回复您的反馈。");
            mBuilder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
            return false;
        } else if (TextUtils.isEmpty(et_feedback.getText().toString())) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("请输入反馈内容");
            mBuilder.setMessage("您好，请填写反馈内容。");
            mBuilder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
            return false;
        } else
            return true;
    }
}

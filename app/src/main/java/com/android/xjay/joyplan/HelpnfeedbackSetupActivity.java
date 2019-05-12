package com.android.xjay.joyplan;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.w3c.dom.Text;

import javax.xml.transform.Templates;

public class HelpnfeedbackSetupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText tv_feedback;
    private EditText tv_mailaddress;
    private Button bt_back;
    private Button bt_confirm;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_helpnfeedback);
        tv_feedback = (EditText) findViewById(R.id.et_setup_feedback);
        tv_mailaddress = (EditText) findViewById(R.id.et_setup_mailaddress);
        bt_back = (Button) findViewById(R.id.bt_setup_helpnfeedback_back);
        bt_confirm = (Button) findViewById(R.id.btn_setup_confirm);
        setListener();
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
}

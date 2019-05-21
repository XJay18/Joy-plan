package com.android.xjay.joyplan;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.xjay.joyplan.Utils.JumpTextWatcher;

import java.util.ArrayList;

public class AddCourseTableActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner chooseSchoolSpinner;
    private ArrayList<String> schools = new ArrayList<>();
    private String chosenSchool;
    private TextView tv_confirm;

    private EditText et_sno;
    private EditText et_name;
    private String sno;
    private String name;

    private LottieAnimationView confirmAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coursetable);

        schools.add("华南理工大学");
        chooseSchoolSpinner = findViewById(R.id.sp_course_choose_school);
        tv_confirm = findViewById(R.id.tv_coursetable_confirm);
        tv_confirm.setOnClickListener(this);
        tv_confirm.setFocusable(true);
        tv_confirm.setFocusableInTouchMode(true);
        findViewById(R.id.ll_course_help).setOnClickListener(this);
        findViewById(R.id.btn_course_back).setOnClickListener(this);
        String[] schoolsArray = (String[]) schools.toArray(new String[schools.size()]);
        ArrayAdapter<String> mSchoolsAdapter = new ArrayAdapter<>(this,
                R.layout.item_select, schoolsArray);
        mSchoolsAdapter.setDropDownViewResource(R.layout.item_dropdown);
        chooseSchoolSpinner.setAdapter(mSchoolsAdapter);
        chooseSchoolSpinner.setSelection(0);
        chooseSchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenSchool = schoolsArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_sno = findViewById(R.id.et_coursetable_sno);
        et_name = findViewById(R.id.et_coursetable_name);
        et_sno.addTextChangedListener(new JumpTextWatcher(this, et_sno, et_name));
        et_name.addTextChangedListener(new JumpTextWatcher(this, et_name, tv_confirm));
        confirmAnimationView = findViewById(R.id.coursetable_anim_okay_blue);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_coursetable_confirm) {
            sno = et_sno.getText().toString();
            name = et_name.getText().toString();
            // TODO
            // CHECK WHETHER THE STUDENT'S COURSE TABLE IS AVAILABLE
            confirmAnimationView.setAnimation("okay_blue.json");
            confirmAnimationView.loop(false);
            Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    confirmAnimationView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    confirmAnimationView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
            confirmAnimationView.addAnimatorListener(mAnimationListener);
            confirmAnimationView.playAnimation();
            sentBroadcast();
            //Toast.makeText(this, "课程表添加成功", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_course_back) {
            finish();
        } else if (v.getId() == R.id.ll_course_help) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("添加课程表");
            mBuilder.setMessage(R.string.info_course);
            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        }
    }


    private void sentBroadcast() {
        Intent intent = new Intent();
        intent.setAction("ADD COURSE TABLE");
        intent.putExtra("sele", "广播测试");
        sendBroadcast(intent);
    }


    // offer the api to add school name
    public boolean addSchoolInArray(String schoolName) {
        if (schools.contains(schoolName)) {
            // not allowed to add the school name which is already in the list.
            return false;
        }
        schools.add(schoolName);
        return true;
    }
}

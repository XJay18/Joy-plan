package com.android.xjay.joyplan;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.xjay.joyplan.Utils.JumpTextWatcher;
import com.android.xjay.joyplan.Utils.POIExcelProcesser;

import android.content.pm.*;
import android.*;

import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        findViewById(R.id.ll_addcoursetable_importexcel).setOnClickListener(this);
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
        else if(v.getId()==R.id.ll_addcoursetable_importexcel){
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            //检测是否有读取外储存的权限，动态获取
            try {
                //检测是否有写的权限
                int permission = ActivityCompat.checkSelfPermission(this,
                        "android.permission.WRITE_EXTERNAL_STORAGE");
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // 没有写的权限，去申请写的权限，会弹出对话框
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            startActivityForResult(intent,1);

       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String path = getPath(this, uri).replaceAll("raw:","");
//                POIExcelProcesser.setExceltoSchedule(/*"/storage/emulated/0/Download/123.xlsx"*/"/data/data/com.android.xjay.joyplan/files/123.xlsx", this);
                POIExcelProcesser.setExceltoSchedule(path, this);
                Toast.makeText(this, "课程表导入成功", Toast.LENGTH_SHORT).show();
                Log.e("path",path);

            }

        }
    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                return id;
//                final Uri contentUri = ContentUris.withAppendedId(
//                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }



        public boolean isDownloadsDocument(Uri uri) {
                return "com.android.providers.downloads.documents".equals(uri.getAuthority());
            }

        public boolean isMediaDocument(Uri uri) {
                return "com.android.providers.media.documents".equals(uri.getAuthority());
            }

        public boolean isExternalStorageDocument(Uri uri) {
        return ("com.android.externalstorage.documents".equals(uri.getAuthority()));
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

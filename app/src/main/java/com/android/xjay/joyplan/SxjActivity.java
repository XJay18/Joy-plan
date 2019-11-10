package com.android.xjay.joyplan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SxjActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Constant, identify take photo
     */
    public static final int TAKE_PHOTO = 1;

    /**
     * Constant, identify choose_photo
     */
    public static final int ChOOSE_PHOTO = 2;

    /**
     * edittext
     */
    private EditText et_content;

    /**
     * to get the img
     */
    private Uri img_uri = null;

    /**
     * button
     */
    private Button btn_takephoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dis_sxj);
        findViewById(R.id.bt_sxj_back).setOnClickListener(this);
        Button button1 = findViewById(R.id.btn_sxj_takephoto);
        Button button2 = findViewById(R.id.btn_sxj_choosephoto);
        Button button3 = findViewById(R.id.btn_sxj_record);
        et_content = findViewById(R.id.et_content);
        button1.setOnClickListener(new NoteOnClickListener());
        button2.setOnClickListener(new NoteOnClickListener());
        button3.setOnClickListener(new NoteOnClickListener());
        loadContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveNoteContnet();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_sxj_back) {
            finish();
        }
    }

    /**
     * Listener class of button
     */
    class NoteOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //如果是拍照
            if (v.getId() == R.id.btn_sxj_takephoto) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    img_uri = FileProvider.getUriForFile(SxjActivity.this,
                            "com.android.xjay.joyplan.fileprovider", outputImage);
                } else {
                    img_uri = Uri.fromFile(outputImage);
                }

                //activate the camera
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, img_uri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
            if (v.getId() == R.id.btn_sxj_choosephoto) {
                choosePhoto();
            }
            //录音
            if (v.getId() == R.id.btn_sxj_record) {

            }
        }

    }

    /**
     * show the img
     */
    private void showRecord(String time) {
        try {
            if (!TextUtils.isEmpty(time)) {
                StringBuffer path = new StringBuffer(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
                path.append("/" + time + ".jpeg");
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path.toString()));
                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                insertImg(this, bitmap, path.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * choose photo
     */
    private void choosePhoto() {
        checkPermission();
    }

    /**
     * get the permission to album
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "您拒绝了打开相册", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;

        }
    }

    /**
     * open album
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, ChOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //文件储存路径
        String path = "";
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmdd-hhmmss");
                    path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + simpleDateFormat.format(new Date()) + "./jpeg";
                    //saveImage(img_uri, path);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(img_uri));
                        insertImg(SxjActivity.this, bitmap, path);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case ChOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    img_uri = data.getData();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
                    path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + simpleDateFormat.format(new Date()) + "./jpeg";
                    //saveImage(img_uri, path);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(img_uri));
                        insertImg(SxjActivity.this, bitmap, path);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * save the img
     */
    private String saveImage(Uri image_uri, String path) {

        try {
//            photos_path.add(path);
//            photos_num++;
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path));
            BitmapFactory.decodeStream(getContentResolver().openInputStream(image_uri)).compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return path;
    }


    /**
     * insert picture into edittext
     *
     * @param context
     * @param bitmap
     * @param imgname
     */
    private void insertImg(Context context, Bitmap bitmap, String imgname) {
        ImageSpan img_span = new ImageSpan(context, bitmap);

        SpannableString spannableString = new SpannableString(imgname);
        spannableString.setSpan(img_span, 0, imgname.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable editable = et_content.getEditableText();
        //获取光标位置
        int index = et_content.getSelectionStart();
        if (index < 0 || index > et_content.length()) {
            editable.append(spannableString);
            Log.e("insert ", "ok");
        } else {
            editable.insert(index, spannableString);
            Log.e("insert ", "o2k");
        }
        et_content.append("\n");
    }


    /**
     * Save All content in notebook
     */
    private void saveNoteContnet() {
        String content = et_content.getText().toString();
        try {
            //check the permission the write external storage
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                //if without permisson, request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String doc_path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(doc_path + "/NoteContent");
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Complete the jobs which are to load data and to show in et_content
     */
    private void loadContent() {
        try {
            //check the permission the write external storage
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                //if without permisson, request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String doc_path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
        File file = new File(doc_path + "/NoteContent");
        if (!file.exists()) {
            Log.e("write", "No file");
            return;
        }
        Log.e("write", "Get file");
        StringBuffer content = new StringBuffer();
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String content_line = null;
            while ((content_line = bufferedReader.readLine()) != null) {
                content.append(content_line + '\n');
            }
            if (content.length() == 0) {
                return;
            }
            String str_content = content.substring(0, content.length() - 1);
            Log.e("write", "get str");
            String regex = "[0-9]{14}.jpeg";
            Matcher matcher = null;
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(str_content);
            while (matcher.find()) {
//                str_content.substring(0,matcher.start());
//                System.out.println(matcher.group());
//                str=str.substring(matcher.end());
//                matcher=Pattern.compile(regex).matcher(str);
            }
            et_content.setText(str_content);
            et_content.setSelection(str_content.length());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

}

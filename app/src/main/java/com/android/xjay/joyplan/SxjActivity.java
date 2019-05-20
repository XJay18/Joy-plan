package com.android.xjay.joyplan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SxjActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int TAKE_PHOTO = 1;
    public static final int ChOOSE_PHOTO = 2;

    private EditText et_content;

    private Uri img_uri = null;

    private Button btn_takephoto;

    private ArrayList<String> photos_path = new ArrayList<>(10);
    private int photos_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dis_sxj);
        findViewById(R.id.bt_sxj_back).setOnClickListener(this);
        Button button1 = (Button) findViewById(R.id.btn_sxj_takephoto);
        Button button2 = (Button) findViewById(R.id.btn_sxj_choosephoto);
        Button button3 = (Button) findViewById(R.id.btn_sxj_record);
        et_content = (EditText) findViewById(R.id.et_content);
        button1.setOnClickListener(new NoteOnClickListener());
        button2.setOnClickListener(new NoteOnClickListener());
        button3.setOnClickListener(new NoteOnClickListener());
        showRecord("1");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_sxj_back){
            finish();
        }
    }

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

    //重新加载之前的内容
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

    //选择图片并插入
    private void choosePhoto() {
        checkPermission();
    }


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

    //打开相册
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
                    saveImage(img_uri, simpleDateFormat.format(new Date()));

                    insertImg(SxjActivity.this, img_uri, path);
                }
                break;

            case ChOOSE_PHOTO:
                if (resultCode == RESULT_OK) {

                    img_uri = data.getData();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");

                    saveImage(img_uri, simpleDateFormat.format(new Date()));
                    insertImg(SxjActivity.this, img_uri, path);
                }
                break;
            default:
                break;
        }
    }

    //向Picture文件中传入图片
    private String saveImage(Uri image_uri, String time) {
        StringBuffer path = new StringBuffer(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());

        path.append("/" + time + ".jpeg");

        try {
            photos_path.add(path.toString());
            photos_num++;

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path.toString()));
            BitmapFactory.decodeStream(getContentResolver().openInputStream(image_uri)).compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return path.toString();
    }

    private void insertImg(Context context, Bitmap bitmap, String path) {
        ImageSpan img_span = new ImageSpan(context, bitmap);

        SpannableString spannableString = new SpannableString("test");

        spannableString.setSpan(img_span, 0, "test".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        Editable editable = et_content.getEditableText();

        //获取光标位置
        int index = et_content.getSelectionStart();
        if (index < 0 || index > et_content.length()) {
            editable.append(spannableString);
        } else {
            editable.insert(index, spannableString);
        }
        et_content.append("\n");

    }

    /*
     * 向Edittext中插入图片
     * 传入图片的Uri
     * */
    private void insertImg(Context context, Uri uri, String path) {
        Bitmap bitmap = null;
        try {
            //将uri转换成Bitmap对象
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        insertImg(context, bitmap, path);
    }

    //Save All content in notebook
    private void saveNoteContnet() {
        String content = et_content.getText().toString();
        if (!TextUtils.isEmpty(content)) {
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
    }

    //Complete the jobs which are to load data and to show in et_content
    private void loadContent(){
        StringBuffer content=null;
        String doc_path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(doc_path + "/NoteContent");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String content_line = null;
            while ((content_line = bufferedReader.readLine()) != null) {
                content.append(content_line);
            }
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

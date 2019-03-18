package com.android.xjay.joyplan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.NotActiveException;


public class SxjActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int ChOOSE_PHOTO = 2;

    private EditText et_content;

    private Uri img_uri = null;

    private Button btn_takephoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dis_sxj);
        Button button1 = (Button) findViewById(R.id.btn_takephoto);
        Button button2 = (Button) findViewById(R.id.btn_choosephoto);
        Button button3 = (Button) findViewById(R.id.btn_record);
        et_content = (EditText) findViewById(R.id.et_content)
        ;
        button1.setOnClickListener(new NoteOnClickListener());
        button2.setOnClickListener(new NoteOnClickListener());
        button3.setOnClickListener(new NoteOnClickListener());
    }

    class NoteOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //如果是拍照
            if (v.getId() == R.id.btn_takephoto) {
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
            if (v.getId() == R.id.btn_choosephoto) {
                choosePhoto();
            }
            //录音
            if (v.getId() == R.id.btn_record) {

            }
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
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(img_uri));
                        insertImg(SxjActivity.this, img_uri);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case ChOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    img_uri = data.getData();
                    insertImg(SxjActivity.this, img_uri);
                }
                break;
            default:
                break;
        }
    }

    /*
     * 向Edittext中插入图片
     * 传入图片的Uri
     * */
    private void insertImg(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            //将uri转换成Bitmap对象
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        //创建ImageSpan对象
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

}

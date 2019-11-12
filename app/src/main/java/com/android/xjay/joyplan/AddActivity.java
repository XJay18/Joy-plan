package com.android.xjay.joyplan;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.Utils.DateFormat;

import java.io.ByteArrayOutputStream;

/**
 * 添加活动activity
 */
public class AddActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * 数据库helper的引用(单例)
     */
    private UserDBHelper mHelper;

    /**
     * 时间选择器开始
     */
    private CustomTimePicker myStartTimePicker;

    /**
     * 时间选择器开始
     */
    private CustomTimePicker myEndTimePicker;

    /**
     * 开始时间文字
     */
    private TextView tv_select_start_time;

    /**
     * 结束时间文字
     */
    private TextView tv_select_end_time;

    /**
     * 活动标题
     */
    private EditText editText_title;

    /**
     * 活动描述
     */
    private EditText editText_description;

    /**
     * 活动地点
     */
    private EditText editText_address;

    /**
     * 当前context
     */
    private Context mContext;

    /**
     * 标题字符串
     */
    private String string_title;

    /**
     * 开始时间字符串
     */
    private String string_start_time;

    /**
     * 结束时间字符串
     */
    private String string_end_time;

    /**
     * 描述字符串
     */
    private String string_description;

    /**
     * 地点字符串
     */
    private String string_address;

    /**
     * 添加按钮
     */
    private Button btn_add;

    /**
     * 取消按钮
     */
    private Button btn_cancel;

    /**
     * 选择照片的状态码
     */
    public static final int CHOOSE_PHOTO = 2;


    /**
     * 添加图片的按钮
     */
    private ImageView img_photo;

    /**
     * 添加图片后显示图片的容器
     */
    private LinearLayout img_add_container;

    /**
     * 添加图片后显示的图片
     */
    private ImageView img_add_image;

    /**
     * 存储添加的图片
     */
    private byte[] img_added;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // 获取当前的context
        mContext = getApplicationContext();

        // 开始时间选择
        tv_select_start_time = findViewById(R.id.tv_select_start_time);

        // 结束时间选择
        tv_select_end_time = findViewById(R.id.tv_select_end_time);


        // 设置时间选择点击函数
        tv_select_start_time.setOnClickListener(this);
        tv_select_end_time.setOnClickListener(this);

        // 初始化时间选择器
        initTimePicker();

        // 数据库操作类的引用（单例）
        mHelper = UserDBHelper.getInstance(this, 1);

        // 找到各个部件
        findAllViews();

        // 注册点击监听
        img_add_image.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
    }

    private void findAllViews() {
        editText_title = findViewById(R.id.editText_title);
        editText_description = findViewById(R.id.editText_detail);
        editText_address = findViewById(R.id.editText_address);
        img_photo = findViewById(R.id.img_photo);
        img_photo.setOnClickListener(this);
        img_add_container = findViewById(R.id.img_add_container);
        btn_add = findViewById(R.id.btn_add);
        img_add_image = findViewById(R.id.img_add_img);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHelper = UserDBHelper.getInstance(mContext, 1);
        mHelper.openWriteLink();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.closeLink();
    }

    /**
     * 点击响应函数
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //取消按钮
            case R.id.btn_cancel: {
                /*Intent intent = new Intent();
                intent.setClass(mContext, ScheduleActivity.class);
                startActivity(intent);*/
                finish();
                break;
            }

            // 添加按钮
            case R.id.btn_add: {

                // 获取用户的输入
                string_title = editText_title.getText().toString();
                string_description = editText_description.getText().toString();
                string_start_time = tv_select_start_time.getText().toString();
                string_end_time = tv_select_end_time.getText().toString();
                string_address = editText_address.getText().toString();

                // 新建一个学生活动信息的实例
                StudentActivityInfo info;

                // 如果添加的图片非空
                if (img_added != null) {
                    info = new StudentActivityInfo(string_title, string_description, string_start_time, string_end_time, string_address, img_added);
                }
                // 添加的图片为空
                else {
                    info = new StudentActivityInfo(string_title, string_description, string_start_time, string_end_time, string_address);

                }
                // 像学生活动表中插入一条新的记录
                mHelper.insert_studentActivity(info);

                // 获取数据库读取权限
                SQLiteDatabase dbRead = mHelper.getReadableDatabase();

                // 查询学生活动表所有记录
                Cursor c = dbRead.query("user_info", null, null, null, null, null, null);
                // 获取结果的长度（即有几条记录）
                Integer i = c.getCount();

                // 添加完后显示目前有多少条记录（测试用）
                Toast toast = Toast.makeText(getApplicationContext(), i.toString(), Toast.LENGTH_SHORT);
                toast.show();
                sentBroadcast();

                //new Thread(new RegThread()).start();


                // 结束当前activity
                finish();
                break;
            }
            // 添加图片按钮
            case R.id.img_add_img:
                // 添加图片后显示的图片
            case R.id.img_photo: {
                // 检查权限
                checkPermission();
                break;
            }
            // 开始时间文字
            case R.id.tv_select_start_time: {
                String temp = tv_select_start_time.getText().toString();
                myStartTimePicker.show(temp);
                break;
            }
            // 结束时间文字
            case R.id.tv_select_end_time: {
                String temp = tv_select_end_time.getText().toString();
                myEndTimePicker.show(temp);
                break;
            }
        }
    }

    /*
    private class RegThread implements Runnable {
        public void run() {
            //获取服务器返回数据
            System.out.println("wenti" + string_title);
            System.out.println("wenti" + string_description);
            System.out.println("wenti" + string_start_time);
            System.out.println("wenti" + string_address);
            String RegRet = WebServicePost.activityPost(string_title,
                    string_start_time, string_description,
                    string_address, "ActiLet");
            //更新UI，界面处理
            //showReq(RegRet);
        }
    }
    */

    /**
     * 添加活动后发送广播
     */
    private void sentBroadcast() {
        Intent intent = new Intent();
        intent.setAction("ADD ACTIVITY");
        intent.putExtra("sele", "广播测试");
        sendBroadcast(intent);
    }


    /**
     * 初始化时间选择器
     */
    private void initTimePicker() {
        //当前时间
        long current_time = System.currentTimeMillis();
        //最大可选结束时间
        long temp = 157680000000L;
        long max_time = current_time + temp;

        //开始时间
        String str_start_time = DateFormat.long2Str(current_time, true);
        tv_select_start_time.setText(str_start_time);

        //结束时间默认比开始时间多4个小时
        long end_beginTime = current_time + 4 * 60 * 60 * 1000;
        String str_end_time = DateFormat.long2Str(end_beginTime, true);

        tv_select_end_time.setText(str_end_time);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        myStartTimePicker = new CustomTimePicker(AddActivity.this,
                new CustomTimePicker.Callback() {
                    @Override
                    public void onTimeSelected(long timestamp) {
                        // remove the first 4 chars i.e. year.
                        String start_time = DateFormat.long2Str(timestamp, true);
                        tv_select_start_time.setText(start_time);

                        long temp_beginTime = timestamp + 60 * 60 * 1000 * 4;
                        String end_time = DateFormat.long2Str(temp_beginTime, true);
                        tv_select_end_time.setText(end_time);

                        myEndTimePicker.setSelectedTime(temp_beginTime, false);
                    }
                }, current_time, max_time, "请选择时间");
        // 允许点击屏幕或物理返回键关闭
        myStartTimePicker.setCancelable(true);
        // 显示时和分
        myStartTimePicker.setTimePickerShowMode(0);
        // 允许循环滚动
        myStartTimePicker.setScrollLoop(true);
        // 允许滚动动画
        myStartTimePicker.setCanShowAnim(true);

        myEndTimePicker = new CustomTimePicker(AddActivity.this, new CustomTimePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String end_time = DateFormat.long2Str(timestamp, true);
                // remove the first 4 chars i.e. year.

                tv_select_end_time.setText(end_time);


            }
        }, current_time, max_time, "请选择时间");
        // 允许点击屏幕或物理返回键关闭
        myEndTimePicker.setCancelable(true);
        // 显示日期和时、分
        myEndTimePicker.setTimePickerShowMode(0);
        // 允许循环滚动
        myEndTimePicker.setScrollLoop(true);
        // 允许滚动动画
        myEndTimePicker.setCanShowAnim(false);
    }

    // 检查权限
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            // 发现没有权限，调用requestPermissions方法向用户申请权限，requestPermissions接收三个参数，第一个是context，第二个是一个String数组，我们把要申请的权限
            // 名放在数组中即可，第三个是请求码，只要是唯一值就行
        } else {
            openAlbum();// 有权限就打开相册
        }
    }

    public void openAlbum() {
        // 通过intent打开相册，使用startactivityForResult方法启动actvity，会返回到onActivityResult方法，所以我们还得复写onActivityResult方法
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    // 弹出窗口向用户申请权限


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);//弹出授权的窗口，这条语句也可以删除，没有影响
        // 获得了用户的授权结果，保存在grantResults中，判断grantResult中的结果来决定接下来的操作
        switch (requestCode) {
            case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "授权失败，无法操作", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnkitKat(data);// 高于4.4版本使用此方法处理图片
                    } else {
                        handleImageBeforeKitKat(data);// 低于4.4版本使用此方法处理图片
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnkitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android,providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    // 获得图片路径
    public String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);   //内容提供器
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));   //获取路径
            }
        }
        cursor.close();
        return path;
    }

    // 展示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitImage = BitmapFactory.decodeFile(imagePath);// 格式化图片
            byte[] img = img(bitImage);
            img_added = img;
            img_photo.setImageBitmap(bitImage);
            img_add_container.setVisibility(View.GONE);
            img_photo.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(AddActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] img(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}

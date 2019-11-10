package com.android.xjay.joyplan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandingList expandingList;


    String[] TITLES = new String[20];

    String[] INFOS = new String[20];

    String[] STARTTIMES = new String[20];

    String[] ADDRESSES = new String[20];

    Drawable[] IMAGES = new Drawable[20];

    private UserDBHelper mHelper;

    Cursor c;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        findViewById(R.id.btn_schedule_back).setOnClickListener(this);
        findViewById(R.id.ll_schedule_help).setOnClickListener(this);

        //get cursor c to get record in DB
        mHelper = UserDBHelper.getInstance(this, 1);
        SQLiteDatabase dbRead = mHelper.getReadableDatabase();
        c = dbRead.query("user_info", null, null
                , null, null, null, null);

        //num of record in DB
        int length = c.getCount();

        c.moveToFirst();

        expandingList = findViewById(R.id.schedule_expanding_list);
        RedrawExpandingList();

        //btn to MainActivity
        Button btn_changeTo_addActivity = findViewById(R.id.changeButton_schedule);
        btn_changeTo_addActivity.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ScheduleActivity.this, AddActivity.class);
                startActivity(intent);
                ScheduleActivity.this.finish();

            }
        });


    }

    private void addItem(String title, String info, String starttime, String address, int colorRes, Drawable drawable) {
        //Let's create an custom_item with R.layout.expanding_layout
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);
        String Date = starttime.substring(0, 10);
        //If custom_item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIcon(drawable);
            item.createSubItems(1);
            final View view = item.getSubItemView(0);
            //Let's set some values in
            configureSubItem(item, view, info);
            //It is possible to get any view inside the inflated layout. Let's set the text in the custom_item
            ((TextView) item.findViewById(R.id.title)).setText(title);
            ((TextView) item.findViewById(R.id.address)).setText(address);
            ((TextView) item.findViewById(R.id.starttime)).setText(Date);
            //We can create items in batch.


            /*item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandingList.removeItem(item);
                }
            });*/
        }
    }

    public Drawable bitmap2Drawable(Bitmap bp) {
        //因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        Bitmap bm = bp;
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        return bd;
    }

    private void addItem(String title, String[] subItems, int colorRes, int iconRes) {
        //Let's create an custom_item with R.layout.expanding_layout
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);

        //If custom_item creation is successful, let's configure it
        if (item != null) {
            //item color
            item.setIndicatorColorRes(colorRes);

            //item picture resource
            item.setIndicatorIconRes(iconRes);

            //It is possible to get any view inside the inflated layout. Let's set the text in the custom_item
            ((TextView) item.findViewById(R.id.title)).setText(title);

            //We can create items in batch.
            item.createSubItems(subItems.length);
            for (int i = 0; i < item.getSubItemsCount(); i++) {
                //Let's get the created custom_sub_item custom_item by its index
                final View view = item.getSubItemView(i);

                //Let's set some values in
                configureSubItem(item, view, subItems[i]);
            }


           /* item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandingList.removeItem(item);
                }
            });*/
        }
    }

    private void configureSubItem(final CustomItem item, final View view, String subTitle) {
        ((TextView) view.findViewById(R.id.sub_title)).setText(subTitle);

    }


    public void RedrawExpandingList() {

        expandingList.Clear_mContainer();

        mHelper = UserDBHelper.getInstance(this, 1);

        ArrayList<StudentActivityInfo> studentActivityInfos;
        studentActivityInfos = mHelper.getAllStudentActivityInfo();
        if (studentActivityInfos != null && studentActivityInfos.size() > 0) {
            int length = studentActivityInfos.size();
            for (int i = 0; i < length; i++) {
                String title = studentActivityInfos.get(i).getTitle();
                String info = studentActivityInfos.get(i).getInfo();
                String starttime = studentActivityInfos.get(i).getStarttime();
                String address = studentActivityInfos.get(i).getAddress();

                byte[] temp = studentActivityInfos.get(i).getImg();

                Drawable drawable;
                if (temp != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                    drawable = bitmap2Drawable(bitmap);

                } else {
                    int res = R.drawable.cc;
                    drawable = this.getResources().getDrawable(res);
                }
                addItem(title, info, starttime, address, R.color.transparent, drawable);
            }
        }


    }

    private void showInsertDialog(final ReserveActivity.OnItemCreated positive) {
        final EditText text = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(text);
        builder.setTitle("enter_title");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positive.itemCreated(text.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_schedule_back) {
            Intent intent = new Intent();
            intent.setClass(this, HomeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ll_schedule_help) {
            android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(this);
            mBuilder.setTitle("发布活动");
            mBuilder.setMessage(R.string.info_fbhd);
            android.app.AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        }
    }

    interface OnItemCreated {
        void itemCreated(String title);
    }

}

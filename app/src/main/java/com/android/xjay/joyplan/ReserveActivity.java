package com.android.xjay.joyplan;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TextView;

import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;

public class ReserveActivity extends AppCompatActivity {


    private ExpandingList expandingList;


    Drawable[] IMAGES = new Drawable[20];

    String[] TITLES = new String[20];

    String[] INFOS = new String[20];
    private UserDBHelper mHelper;

    Cursor c;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reserve);

        mHelper = UserDBHelper.getInstance(this, 1);
        SQLiteDatabase dbRead = mHelper.getReadableDatabase();
        c = dbRead.query("user_info", null, null
                , null, null, null, null);

        int length = c.getCount();
        c.moveToFirst();

        expandingList = findViewById(R.id.reserve_expanding_list);
        expandingList.setItemPadding(0);

        for (int i = 0; i < length; i++) {
            TITLES[i] = c.getString(1);
            INFOS[i] = c.getString(2);
            byte[] temp = c.getBlob(c.getColumnIndex("img"));

            Drawable drawable;
            if (temp != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                drawable = bitmap2Drawable(bitmap);
                IMAGES[i] = drawable;
            } else {
                int res = R.drawable.cc;
                drawable = this.getResources().getDrawable(res);
                IMAGES[i] = drawable;
            }
            String[] s = new String[]{INFOS[i]};
            addItem(TITLES[i], s, R.color.colorWhite, IMAGES[i]);
            c.move(1);
        }


        //createItems();


        //Button btn_changeTo_addActivity = findViewById(R.id.changeButton_reserve);
        /*btn_changeTo_addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ReserveActivity.this, AddActivity.class);
                startActivity(intent);
                ReserveActivity.this.finish();
            }
        });*/


    }


    public Drawable bitmap2Drawable(Bitmap bp) {
        //因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        Bitmap bm = bp;
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        return bd;
    }

    private void addItem(String title, String[] subItems, int colorRes, Drawable drawable) {
        //Let's create an custom_item with R.layout.expanding_layout
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);

        //If custom_item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIcon(drawable);
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


            /*item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
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

    private void showInsertDialog(final OnItemCreated positive) {
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

    interface OnItemCreated {
        void itemCreated(String title);
    }
}
package com.android.xjay.joyplan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;

public class ReserveActivity extends AppCompatActivity {


    private ExpandingList expandingList;

    //TODO
    int[] IMAGES = {R.drawable.cc, R.drawable.cc};

    String[] TITLES = new String[20];

    String[] INFOS = new String[20];
    private UserDBHelper_schedule mHelper;

    Cursor c;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        mHelper = UserDBHelper_schedule.getInstance(this, 1);
        SQLiteDatabase dbRead = mHelper.getReadableDatabase();
        c = dbRead.query("user_info", null, null
                , null, null, null, null);

        int length = c.getCount();
        c.moveToFirst();

        expandingList = findViewById(R.id.reserve_expanding_list);
        expandingList.setItemPadding(0);
        int iconRes=R.drawable.duck;
        for (int i = 0; i < length; i++) {
            TITLES[i] = c.getString(1).toString();
            INFOS[i] = c.getString(2).toString();
            String[] s=new String[]{INFOS[i],INFOS[i],INFOS[i],INFOS[i]};
            addItem(TITLES[i],s,R.color.colorWhite,iconRes);
            c.move(1);
        }


        //createItems();


        Button btn_changeTo_addActivity = findViewById(R.id.changeButton_reserve);
        btn_changeTo_addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ReserveActivity.this, AddActivity.class);
                startActivity(intent);
                ReserveActivity.this.finish();
            }
        });


    }

    private void createItems() {
        int iconRes=R.drawable.cc;
        addItem("Title", new String[]{"House will be rented", "Boat", "Candy", "Collection", "Sport", "Ball", "Head"}, R.color.colorAccent, iconRes);
        addItem("Title", new String[]{"Dog", "Horse", "Boat"}, R.color.colorWhite, R.drawable.ic_ghost);
        addItem("Title", new String[]{"Cat"}, R.color.colorWhite, R.drawable.ic_ghost);
        addItem("Title", new String[]{"Parrot", "Elephant", "Coffee"}, R.color.colorWhite, R.drawable.ic_ghost);
        addItem("Title", new String[]{}, R.color.colorAccent, R.drawable.ic_ghost);
        addItem("Title", new String[]{"Golf", "Football"}, R.color.colorAccent, R.drawable.ic_ghost);
        addItem("Title", new String[]{"Ferrari", "Mazda", "Honda", "Toyota", "Fiat"}, R.color.colorAccent, R.drawable.ic_ghost);
        addItem("Title", new String[]{"Beans", "Rice", "Meat"}, R.color.colorAccent, R.drawable.ic_ghost);
        addItem("Title", new String[]{"Hamburger", "Ice cream", "Candy"}, R.color.colorAccent, R.drawable.ic_ghost);
    }


    private void addItem(String title, String[] subItems, int colorRes, int iconRes) {
        //Let's create an custom_item with R.layout.expanding_layout
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);

        //If custom_item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
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
            item.findViewById(R.id.add_more_sub_items).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInsertDialog(new OnItemCreated() {
                        @Override
                        public void itemCreated(String title) {
                            View newSubItem = item.createSubItem();
                            configureSubItem(item, newSubItem, title);
                        }
                    });
                }
            });

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
        view.findViewById(R.id.remove_sub_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.removeSubItem(view);
            }
        });
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
package com.android.xjay.joyplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
   Include four instances of the home pages.
   Agenda, Planning, Discovery, Setup
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    protected Context mContext;

    public static HomeFragment newInstance(String info) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mContext=getActivity();
        // get the name of this fragment (Agenda or Planning or Discovery or Setup)
        String info=getArguments().getString("info");
        switch (info){
            // deal with the fragment_discovery
            case "发现":
            {
                View view = inflater.inflate(R.layout.fragment_discovery, null);
                view.findViewById(R.id.ll_fqz).setOnClickListener(this);
                view.findViewById(R.id.ll_ydhd).setOnClickListener(this);
                view.findViewById(R.id.ll_rcq).setOnClickListener(this);
                view.findViewById(R.id.ll_sxj).setOnClickListener(this);
                return view;
            }
            case "规划":
            {
                View view=inflater.inflate(R.layout.fragment_plan,null);
                return view;
            }
            default:{
                View view = inflater.inflate(R.layout.fragment_base, null);
                TextView tvInfo = (TextView) view.findViewById(R.id.textView);
                tvInfo.setText(getArguments().getString("info"));
                tvInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
                    }
                });
                return view;
            }
        }
    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.ll_fqz){
            Toast.makeText(mContext,"你点击了番茄钟",Toast.LENGTH_SHORT).show();
        }else if(view.getId()==R.id.ll_ydhd) {
            Toast.makeText(mContext,"你点击了预定活动",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.setClass(this.getContext(),ScheduleActivity.class);
            startActivity(intent);


        }else if(view.getId()==R.id.ll_rcq){
            Toast.makeText(mContext,"你点击了日程圈",Toast.LENGTH_SHORT).show();
        }else if(view.getId()==R.id.ll_sxj){
            Toast.makeText(mContext,"你点击了随心记",Toast.LENGTH_SHORT).show();
        }
    }
}
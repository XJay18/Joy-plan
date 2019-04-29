package com.android.xjay.joyplan.Calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.xjay.joyplan.R;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {

    private final View.OnLongClickListener longClickListener;
    private final View.OnClickListener clicklistener;
    List<String> list;

    public CustomListAdapter(View.OnClickListener clicklistener,View.OnLongClickListener longClickListener,List<String> list){
        this.clicklistener=clicklistener;
        this.longClickListener=longClickListener;
        this.list=list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_example,parent,false);
            holder.button=(Button)convertView.findViewById(R.id.btn_mission);
            holder.textView=(TextView)convertView.findViewById(R.id.tv_mission) ;
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.button.setOnClickListener(clicklistener);
        holder.button.setOnLongClickListener(longClickListener);
        holder.button.setTag(position);
        holder.textView.setText(list.get(position));

        return convertView;
    }
    class ViewHolder{
        Button button;
        TextView textView;
    }
}

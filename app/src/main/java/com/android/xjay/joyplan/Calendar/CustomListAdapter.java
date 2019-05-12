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
    private int tag;
    List<String> mlist;
    List<Integer> heightList;



    public CustomListAdapter(View.OnClickListener clicklistener,View.OnLongClickListener longClickListener,List<String> list,List<Integer> heightList,int tag){
        this.clicklistener=clicklistener;
        this.longClickListener=longClickListener;
        this.mlist =list;
        this.heightList=heightList;
        this.tag=tag;

    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
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
        holder.button.setTag(tag*10+position);
        holder.textView.setText(mlist.get(position));
        if(mlist.get(position)==""){
            holder.button.setBackgroundResource(R.drawable.btn_shape_agenda_white);
        }
        else{
            holder.button.setBackgroundResource(R.drawable.btn_shape_agenda_blue);
        }

        return convertView;
    }
    class ViewHolder{
        Button button;
        TextView textView;
    }


}

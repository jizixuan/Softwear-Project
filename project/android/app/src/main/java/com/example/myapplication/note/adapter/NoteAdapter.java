package com.example.myapplication.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.BillAdapter;
import com.example.myapplication.note.entity.NoteItem;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter {
    List<NoteItem> bills = new ArrayList<>();
    private Context mContext;
    private int layOut;

    public NoteAdapter(List<NoteItem> items, Context mContext, int layOut) {
        this.bills = items;
        this.mContext = mContext;
        this.layOut = layOut;
    }

    @Override
    public int getCount() {
        if(bills!=null){
            return bills.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (bills!=null){
            return bills.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //获取组件
        NoteAdapter.ViewHolder holder=null;
        if(null==holder){
            //加载item的布局文件
            LayoutInflater inflater=LayoutInflater.from(mContext);
            view=inflater.inflate(layOut,null);
            holder=new NoteAdapter.ViewHolder();
            holder.note=view.findViewById(R.id.tv_note_);
            holder.date=view.findViewById(R.id.tv_date_note);
            view.setTag(holder);
        }else{
            holder= (NoteAdapter.ViewHolder) view.getTag();
        }
        holder.note.setText(bills.get(i).getNote());
        holder.date.setText(bills.get(i).getDate()+"");
        return view;
    }
    private class ViewHolder{
        TextView note;
        TextView date;
    }
}

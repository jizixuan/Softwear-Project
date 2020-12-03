package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends BaseAdapter {
    private List<BillItem> bills=new ArrayList<>();
    private Context mContext;
    public BillAdapter(Context mContext){
        super();
        this.mContext = mContext;
    }


    public BillAdapter(Context mContext, int date_bill_item, List<DateBill> dateBillList) {
        super();
        this.mContext = mContext;
    }

    public void addAll(List<BillItem> bills){
        this.bills=bills;
        notifyDataSetChanged();

    }
    public void clearAll() {
        this.bills.clear();
        notifyDataSetChanged();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        //获取组件
        ViewHolder holder=null;
        if(null==holder){
            //加载item的布局文件
            LayoutInflater inflater=LayoutInflater.from(mContext);
            view=inflater.inflate(R.layout.bill_item,null);
            holder=new ViewHolder();
            holder.img=view.findViewById(R.id.lv_img);
            holder.type=view.findViewById(R.id.lv_type);
            holder.num=view.findViewById(R.id.lv_num);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.type.setText(bills.get(position).getType());
        holder.num.setText(bills.get(position).getNum()+"");
        Glide.with(mContext)
                .load(bills.get(position).getImg())
                .into(holder.img);
        return view;
    }
    private class ViewHolder{
        ImageView img;
        TextView type;
        TextView num;
    }
}

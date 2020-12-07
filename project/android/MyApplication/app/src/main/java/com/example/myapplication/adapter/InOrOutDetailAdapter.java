package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.entity.IoDetail;
import com.liulishuo.magicprogresswidget.MagicProgressBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InOrOutDetailAdapter extends BaseAdapter {
    private List<IoDetail> ioDetails=new ArrayList<>();
    private Context mContext;
    private int layout;

    public InOrOutDetailAdapter() {
    }

    public InOrOutDetailAdapter(List<IoDetail> ioDetails, Context mContext, int layout) {
        this.ioDetails = ioDetails;
        this.mContext = mContext;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        if(ioDetails!=null){
            return ioDetails.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (ioDetails!=null){
            return ioDetails.get(i);
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
            view=inflater.inflate(layout,null);
            holder= new ViewHolder();
            holder.date=view.findViewById(R.id.io_detail_adapter_date);
            holder.type=view.findViewById(R.id.io_detail_adapter_type);
            holder.percentage=view.findViewById(R.id.io_detail_adapter_percentage);
            holder.img=view.findViewById(R.id.io_detail_adapter_img);
            holder.num=view.findViewById(R.id.io_detail_adapter_num);
            holder.progress=view.findViewById(R.id.io_detail_adapter_progress);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        //设置日期
        Date date = ioDetails.get(position).getDate();
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        final String str = format.format(date);
        holder.date.setText(str);
        //设置类型
        holder.type.setText(ioDetails.get(position).getType());
        //设置金额
        holder.num.setText(ioDetails.get(position).getNum()+"");
        //设置图片
        holder.img.setImageBitmap(ioDetails.get(position).getImg());
        //设置百分比
        holder.percentage.setText(ioDetails.get(position).getPercent()+"%");
        //设置进度条百分比
//        holder.progress.setPercent( ioDetails.get(position).getProgress());
        holder.progress.setSmoothPercent(ioDetails.get(position).getProgress()/100,800);
        return view;
    }
    private class ViewHolder{
        TextView type;
        TextView percentage;
        TextView num;
        MagicProgressBar progress;
        TextView date;
        ImageView img;
    }
}

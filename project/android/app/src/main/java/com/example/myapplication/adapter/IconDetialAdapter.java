package com.example.myapplication.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.myapplication.BillDetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.entity.IconItem;
import com.example.myapplication.entity.IconList;
import com.example.myapplication.util.ConfigUtil;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IconDetialAdapter extends BaseAdapter {
    private List<IconList> dateBills=new ArrayList<>();
    private Context mContext;
    private int layout;
    public ViewHolder holder;
    public IconDetialAdapter(){
    }
    public IconDetialAdapter(List<IconList> dateBills, Context mContext, int layout) {
        this.dateBills = dateBills;
        this.mContext = mContext;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        if(dateBills!=null){
            return dateBills.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (dateBills!=null){
            return dateBills.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        //获取组件
        holder=null;
        if(null==holder){
            //加载item的布局文件
            LayoutInflater inflater=LayoutInflater.from(mContext);
            view=inflater.inflate(layout,null);
            holder=new IconDetialAdapter.ViewHolder();
            holder.listView=view.findViewById(R.id.lv_fragment);
            view.setTag(holder);
        }else{
            holder= (IconDetialAdapter.ViewHolder) view.getTag();
        }
//        //设置星期
//        Date date = dateBills.get(position).getDate();
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        final String str = format.format(date);
//        //设置日期
//        Date date1 = dateBills.get(position).getDate();
//        DateFormat format1 = new SimpleDateFormat("MM月dd日");
//        final String str1 = format1.format(date1);
        //设置子listview
        holder.daAdapter=new IconItemAdapter(mContext);
        holder.daAdapter.addAll(dateBills.get(position).getBills());
        holder.listView.setAdapter(holder.daAdapter);
        ConfigUtil.setListViewHeightBasedOnChildren(holder.listView);
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                IconItem item=dateBills.get(position).getBills().get(i);
                intent.putExtra("type",item.getType());
                Log.e("IconDetialAdapter",item.getDate()+"   "+Week(item.getDate()));
                intent.putExtra("num",item.getNum());
                intent.putExtra("numType",item.getNumType());
                intent.putExtra("note",item.getNote());
                intent.putExtra("date",item.getDate()+"   "+Week(item.getDate()));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                item.getImg().compress(Bitmap.CompressFormat.PNG, 100, out);
                byte[] array= out.toByteArray();
                intent.putExtra("bitmap",array);
                intent.setClass(mContext, BillDetailsActivity.class);
                intent.putExtra("transition", "explode");
                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
//                mContext.startActivity(intent);
            }
        });
        return view;
    }
    public class ViewHolder{
        ListView listView;
        IconItemAdapter daAdapter;
    }

    /**
     * 计算星期
     * @param dateTime
     * @return
     */
    private int getDayofWeek(String dateTime) {

        Calendar cal = Calendar.getInstance();
        if (dateTime.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTime);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            if (date != null) {
                cal.setTime(new Date(date.getTime()));
            }
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    private String Week(String dateTime) {
        String week = "";
        switch (getDayofWeek(dateTime)) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }
}

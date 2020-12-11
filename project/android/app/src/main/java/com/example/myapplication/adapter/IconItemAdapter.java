package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entity.IconItem;
import com.liulishuo.magicprogresswidget.MagicProgressBar;

import java.util.ArrayList;
import java.util.List;

public class IconItemAdapter extends BaseAdapter {
    private List<IconItem> bills=new ArrayList<>();
    private Context mContext;
    public IconItemAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void addAll(List<IconItem> bills){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        //加载组件
        ViewHolder holder = null;
        if(holder == null){
            //加载item布局
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.icon_itm_adapter,null);
            holder = new ViewHolder();
            holder.img = view.findViewById(R.id.lv_img_icon);
            holder.num = view.findViewById(R.id.lv_num_icon);
            holder.date = view.findViewById(R.id.tv_date_icon);
            holder.percentage = view.findViewById(R.id.tv_icon_p);
            holder.pb = view.findViewById(R.id.pb_bar);
            holder.type = view.findViewById(R.id.lv_type_icon);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        int count = 0;
        for(int j=0;j<bills.size();j++){
            count+=bills.get(j).getNum();
        }
        double c = (bills.get(i).getNum()*100/count);
        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.00");
        java.text.DecimalFormat df = new java.text.DecimalFormat("0");
        String str = myformat.format(c);
        String str1 = df.format(c);
//        Log.e("lww",str1);
        holder.percentage.setText(str+"%");
        if(i == 0){
            holder.pb.setSmoothPercent(1,800);
        }else{
            holder.pb.setSmoothPercent((float) Integer.parseInt(str1)/100,800);
        }
        holder.type.setText(bills.get(i).getType());
        holder.num.setText(bills.get(i).getNum()+"");
        holder.date.setText(bills.get(i).getDate());
        Glide.with(mContext)
                .load(bills.get(i).getImg())
                .into(holder.img);
        return view;
    }
    private class ViewHolder{
        ImageView img;
        TextView type;
        TextView num;
        MagicProgressBar pb;
        TextView percentage;//百分比
        TextView date;
    }
}

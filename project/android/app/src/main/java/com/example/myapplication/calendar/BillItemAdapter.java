package com.example.myapplication.calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.myapplication.R;
import com.example.myapplication.calendar.group.GroupRecyclerAdapter;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BillItemAdapter extends GroupRecyclerAdapter<String, BillItem> {
    private RequestManager mLoader;
    public BillItemAdapter(Context context) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<BillItem>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("今日情况",create());
        titles.add("今日情况");
        resetGroups(map,titles);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new BillItemViewHolder(mInflater.inflate(R.layout.bill_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, BillItem item, int position) {
        BillItemViewHolder h = (BillItemViewHolder) holder;
        h.mTextType.setText(item.getType());
        if (item.getNumType().equals("+")){
            h.mTextNum.setText("+"+item.getNum());
        }else{
            h.mTextNum.setText("-"+item.getNum());
        }

        mLoader.load(item.getImg())
                .into(h.mImageView);
    }
    private static class BillItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextType,
                mTextNum;
        private ImageView mImageView;

        private BillItemViewHolder(View itemView) {
            super(itemView);
            mTextType = itemView.findViewById(R.id.lv_type);
            mTextNum = itemView.findViewById(R.id.lv_num);
            mImageView = itemView.findViewById(R.id.lv_img);
        }
    }
    private static List<BillItem> create() {
        Bitmap bitmap = ImageUtils.getBitmap(R.drawable.bg_material);
        List<BillItem> list = new ArrayList<>();
        BillItem bill1 = new BillItem(bitmap,"食品",2,"1","吃饭");
        BillItem bill2 = new BillItem(bitmap,"食品",2,"1","吃饭");
        BillItem bill3 = new BillItem(bitmap,"食品",2,"1","吃饭");
        list.add(bill1);
        list.add(bill2);
        list.add(bill3);

        //将接受到数据直接返回
        return list;
    }
}

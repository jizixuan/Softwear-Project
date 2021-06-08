package com.example.myapplication.calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
    public BillItemAdapter(Context context,List<com.example.myapplication.entity.BillItem> billItems) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<BillItem>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        List<BillItem> billItems1  = new ArrayList<>();
        for (com.example.myapplication.entity.BillItem billItem : billItems){
            BillItem billItem1 = new BillItem();
            billItem1.setId(billItem.getId());
            billItem1.setImg(billItem.getImg());
            billItem1.setType(billItem.getType());
            billItem1.setNote(billItem.getNote());
            billItem1.setNum(billItem.getNum());
            billItem1.setNumType(billItem.getNumType());
            billItems1.add(billItem1);
        }
        map.put("今日情况",billItems1);
        Log.i("lr", "BillItemAdapter: listview");
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
        Log.i("type", "onBindViewHolder: "+item.getType());
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
}

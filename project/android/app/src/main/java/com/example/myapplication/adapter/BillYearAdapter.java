package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.entity.DateBill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BillYearAdapter extends BaseAdapter {
    private Context mContext;
    private int itemLayoutId;
    private List<DateBill> dateBillList = new ArrayList<>();

    public BillYearAdapter(Context mContext, int itemLayoutId, List<DateBill> dateBillList) {
        this.mContext = mContext;
        this.itemLayoutId = itemLayoutId;
        this.dateBillList = dateBillList;
    }

    @Override
    public int getCount() {
        if (dateBillList != null){
            return dateBillList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (dateBillList != null){
            return  dateBillList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(itemLayoutId,null);
        TextView tv_Month = view.findViewById(R.id.tv_billMonth);
        TextView tv_Income = view.findViewById(R.id.tv_billIncome1);
        TextView tv_Outcome = view.findViewById(R.id.tv_billOutcome1);
        TextView tv_Balance = view.findViewById(R.id.tv_billBalance1);
        SimpleDateFormat format = new SimpleDateFormat("MM");
        tv_Month.setText(format.format(dateBillList.get(i).getDate())+"月");
        tv_Income.setText(dateBillList.get(i).getIncome()+"");
        tv_Outcome.setText(dateBillList.get(i).getExpenditure()+"");

        double balance = dateBillList.get(i).getIncome()-dateBillList.get(i).getExpenditure();
        if (balance>=0){
            tv_Balance.setText(balance+"");
        }else{
            tv_Balance.setText("-"+balance+"");
        }
        return view;
    }
}

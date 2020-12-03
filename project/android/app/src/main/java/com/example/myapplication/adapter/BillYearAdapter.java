package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.DateBill;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class BillYearAdapter extends BaseAdapter {
    private Context mContext;
    private int itemLayoutId;
    private List<DateBill> dateBills = new ArrayList<>();

    public BillYearAdapter(Context mContext, int itemLayoutId, List<DateBill> dateBills) {
        this.mContext = mContext;
        this.itemLayoutId = itemLayoutId;
        this.dateBills = dateBills;
    }

    @Override
    public int getCount() {
        if (dateBills != null){
            return dateBills.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (dateBills != null){
            return  dateBills.get(i);
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
        tv_Month.setText(dateBills.get(i).getDate().toString());
        tv_Income.setText(dateBills.get(i).getIncome()+"");
        tv_Outcome.setText(dateBills.get(i).getExpenditure()+"");
        double balance = dateBills.get(i).getIncome()-dateBills.get(i).getExpenditure();
        tv_Balance.setText(balance+"");
        return view;
    }
}

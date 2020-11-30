package com.example.myapplication.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.BillTypeAdapter;
import com.example.myapplication.entity.BillType;

import java.util.ArrayList;
import java.util.List;

public class AddExpenditureFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<BillType> billTypes=new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_expenditure,container,false);
        //初始化示图
        initViews();
        initData();
        return view;
    }

    private void initData() {
        BillType billType1=new BillType(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"购物1");
        BillType billType2=new BillType(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"购物2");
        BillType billType3=new BillType(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"购物3");
        BillType billType4=new BillType(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"购物4");
        BillType billType5=new BillType(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"购物5");
        BillType billType6=new BillType(BitmapFactory.decodeResource(getResources(),R.mipmap.type_shopping),"购物6");
        billTypes.add(billType1);
        billTypes.add(billType2);
        billTypes.add(billType3);
        billTypes.add(billType4);
        billTypes.add(billType5);
        billTypes.add(billType6);
        BillTypeAdapter adapter=new BillTypeAdapter(billTypes);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BillTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void initViews() {
        recyclerView=view.findViewById(R.id.add_expenditure_rv);
        recyclerView.setLayoutManager(new GridLayoutManager (getActivity (),4,GridLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator (new DefaultItemAnimator());
    }
}

package com.example.myapplication.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.myapplication.BillActivity;
import com.example.myapplication.BudgetActivity;
import com.example.myapplication.ChangeInfoActvity;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.view.Gua;
import com.example.myapplication.view.ObservableScrollView;

public class PersonalFragment extends Fragment {
    private ImageView imgPhoto;//头像
    private TextView tvName;//账户名
    private TextView tvNum;//记账笔数

    private RelativeLayout relativeVip;//vip部分relativeLayout

    private RelativeLayout relativeBill;
    private TextView tvMonth1;//当前账单月份
    private TextView tvIncome;//本月收入
    private TextView tvOutcome;//本月支出
    private TextView tvBalance;//本月结余

    private RelativeLayout relativeBudget;//本月预算
    private TextView tvMonth;//当前预算月份
    private TextView tvBudget1;//本月总预算
    private TextView tvBudget;//本月剩余预算
    private TextView tvOut;//本月支出

    private ObservableScrollView scrollView;//滑动控件
    private RelativeLayout relative;//头像部分relativeLayout
    private TextView textView;//标题
    private int relativeHeight;//头像部分高度
    private View view;
    private Gua mGua;
    private Button btnSettings;
    private Button btnChange;
    private Button btnExit;
    private int ratio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal,container,false);
        //获取控件
        initViews();
        //设置预算比
        setGua(ratio);
        //设置滑动监听
        initListeners();
        setListener();
        return view;
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        MyListener myListener = new MyListener();
        relativeBudget.setOnClickListener(myListener);
        btnChange.setOnClickListener(myListener);
        relativeBill.setOnClickListener(myListener);
        btnExit.setOnClickListener(myListener);
    }

    /**
     * 设置预算比图形
     * @param ratio
     */
    private void setGua(int ratio) {
        mGua.setTargetPercent(ratio);
    }

    private void initViews() {
        imgPhoto = view.findViewById(R.id.personalImg);
        tvName = view.findViewById(R.id.tv_personalName);
        tvNum = view.findViewById(R.id.tv_personalNum);

        relativeVip = view.findViewById(R.id.personalVip);

        relativeBill = view.findViewById(R.id.personalBill);
        tvMonth1 = view.findViewById(R.id.tv_personalMonth1);
        tvIncome = view.findViewById(R.id.tv_personalIncome);
        tvOutcome = view.findViewById(R.id.tv_personalOutcome);
        tvBalance = view.findViewById(R.id.tv_personalBalance);

        relativeBudget = view.findViewById(R.id.personalBudget);
        tvMonth = view.findViewById(R.id.tv_personalMonth);
        tvBudget = view.findViewById(R.id.tv_personalBudget);
        tvBudget1 = view.findViewById(R.id.tv_personalBudget1);
        tvOut = view.findViewById(R.id.tv_personalOut);

        btnChange = view.findViewById(R.id.btn_personalChange);
        btnExit = view.findViewById(R.id.btn_personalExit);
        btnSettings = view.findViewById(R.id.btn_personalSettings);

        textView = view.findViewById(R.id.textview);
        scrollView = view.findViewById(R.id.scrollview);
        relative = view.findViewById(R.id.personal);
        mGua = view.findViewById(R.id.Circle);

    }

    private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = relative.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relative.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                relativeHeight = relative.getHeight();
                scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        // Log.i("TAG", "y--->" + y + "    height-->" + height);
                        if (y <= 0) {
//                          设置文字背景颜色，白色
                            textView.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
//                          设置文字颜色，黑色
                            textView.setTextColor(Color.argb((int) 0, 255, 255, 255));
                            Log.e("111","y <= 0");
                        } else if (y > 0 && y <= relativeHeight) {
                            float scale = (float) y / relativeHeight;
                            float alpha = (255 * scale);
                            // 只是layout背景透明(仿知乎滑动效果)白色透明
                            textView.setBackgroundColor(Color.argb((int) alpha, 0, 145, 252));
                            //                          设置文字颜色，黑色，加透明度
                            textView.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                            Log.e("111","y > 0 && y <= imageHeight");
                        } else {
//							白色不透明
                            textView.setBackgroundColor(Color.argb((int) 255, 0, 145, 252));
                            //                          设置文字颜色
                            //黑色
                            textView.setTextColor(Color.argb((int) 255, 0, 0, 0));
                            Log.e("111","else");
                        }
                    }
                });

            }
        });
    }
    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.personalBudget:
                    Intent i = new Intent();
                    i.setClass(view.getContext(), BudgetActivity.class);
                    startActivity(i);
                    break;
                case R.id.btn_personalChange:
                    Intent i1 = new Intent();
                    i1.setClass(view.getContext(), ChangeInfoActvity.class);
                    startActivity(i1);
                    break;
                case R.id.personalBill:
                    Intent i2 = new Intent();
                    i2.setClass(view.getContext(), BillActivity.class);
                    startActivity(i2);
                    break;

                case R.id.btn_personalExit:
                    deleteData();
                    break;
            }
        }
    }

    private void deleteData() {
        SharedPreferences sd = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sd.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent();
        intent.setClass(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}

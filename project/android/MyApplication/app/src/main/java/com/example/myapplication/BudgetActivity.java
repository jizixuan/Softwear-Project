package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.myapplication.util.TakeBudgetPopWin;

public class BudgetActivity extends AppCompatActivity {
    private EditText edtBudget;//输入的预算
    private Button btnConfirm;//确认预算
    private Button btnCancel;//取消预算
    private String str;//输入预算字符串
    private RelativeLayout budget;//预算
    private PopupWindow mPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showAlertDialog();
        setContentView(R.layout.activity_budget);
        budget = findViewById(R.id.budget);
        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopFormBottom(view);
            }
        });
    }

    private void showAlertDialog() {
        //创建Builder对象
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框属性
        final View view = getLayoutInflater().inflate(R.layout.budget_message,null);
        builder.setView(view);
        edtBudget = view.findViewById(R.id.edt_dialogBudget);

        btnConfirm = view.findViewById(R.id.btn_dialogConfirm);
        btnCancel = view.findViewById(R.id.btn_dialogCancel);
        btnConfirm.setEnabled(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();//显示弹窗
        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //boolean tag1 = false;
                boolean tag = edtBudget.getText().length()>0;
                str = edtBudget.getText().toString();
//                if (str.equals("") && str.equals("0")){
//                    tag1 = true;
//                }
                if(tag){
                    btnConfirm.setEnabled(true);
                    btnConfirm.setTextColor(getResources().getColor(R.color.bule));
                }else{
                    btnConfirm.setEnabled(false);
                    btnConfirm.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    public void showPopFormBottom(View view) {
        TakeBudgetPopWin takePhotoPopWin = new TakeBudgetPopWin(this, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        takePhotoPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_changeBudget:
                    System.out.println("btn_take_photo");
                    break;
                case R.id.btn_deleteBudget:
                    System.out.println("btn_pick_photo");
                    break;
            }
        }
    };
}

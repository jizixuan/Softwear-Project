package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BillDetailsActivity extends AppCompatActivity {
    private TextView type;
    private TextView numType;
    private TextView num;
    private TextView date;
    private TextView note;
    private ImageView re;
    private ImageView img;
    private Button edit;
    private Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        Explode explode = new Explode();
        explode.setDuration(450);
        getWindow().setEnterTransition(explode);
        getView();
        initData();
        setListener();
    }

    private void setListener() {
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initData() {
        Intent request=getIntent();
        type.setText(request.getStringExtra("type"));
        if(request.getStringExtra("numType").equals("+")){
            numType.setText("收入");
        }else{
            numType.setText("支出");
        }
        num.setText(request.getDoubleExtra("num",0)+"");
        date.setText(request.getStringExtra("date"));
        note.setText(request.getStringExtra("note"));
        byte[] data=request.getByteArrayExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        img.setImageBitmap(bitmap);
    }

    private void getView() {
        type=findViewById(R.id.bill_detail_type);
        numType=findViewById(R.id.bill_detail_numType);
        num=findViewById(R.id.bill_detail_num);
        date=findViewById(R.id.bill_detail_date);
        note=findViewById(R.id.bill_detail_note);
        re=findViewById(R.id.bill_detail_re);
        img=findViewById(R.id.bill_detail_img);
        edit=findViewById(R.id.bill_detail_edit);
        delete=findViewById(R.id.bill_detail_delete);
    }

}

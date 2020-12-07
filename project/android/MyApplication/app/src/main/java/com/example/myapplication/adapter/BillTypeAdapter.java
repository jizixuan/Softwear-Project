package com.example.myapplication.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.BillType;

import java.util.ArrayList;
import java.util.List;

public class BillTypeAdapter  extends RecyclerView.Adapter<BillTypeAdapter.ViewHolder>{
    private onRecyclerItemClickerListener m_listener;

    private List<BillType> billTypes=new ArrayList<>();

    public BillTypeAdapter(List<BillType> billTypes) {
        this.billTypes = billTypes;
    }

    /**
     * 设置回调监听
     *
     * @param
     */
    public void setItemListener(onRecyclerItemClickerListener mListener) {
        this.m_listener = mListener;
    }

    @NonNull
    @Override
    public BillTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_type, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillTypeAdapter.ViewHolder holder, int position) {
        holder.typeName.setText(billTypes.get(position).getName());
        holder.img.setImageBitmap(billTypes.get(position).getImg());
        holder.ll.setOnClickListener(getOnClickListener(position));
        Resources resources = holder.context.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.my_back);
        Drawable drawable1 = resources.getDrawable(R.drawable.my_back1);
        if(getTypeName0()!=null){
            if(getTypeName0().equals((billTypes.get(position).getName()))){
                holder.rl.setBackground(drawable1);
                typeName0=null;
            }
        }else {
            if (position == getmPosition()) {
                holder.rl.setBackground(drawable1);
            } else {
//            否则的话就全白色初始化背景
                holder.rl.setBackground(drawable);
            }
        }
    }

    @Override
    public int getItemCount() {
        return billTypes == null ? 0 : billTypes.size();

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView typeName;
        ImageView img;
        LinearLayout ll;
        RelativeLayout rl;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeName=itemView.findViewById(R.id.bill_type_name);
            img=itemView.findViewById(R.id.bill_type_img);
            ll=itemView.findViewById(R.id.bill_type);
            rl=itemView.findViewById(R.id.bill_type_rl);
            context=itemView.getContext();
        }
    }
    public interface onRecyclerItemClickerListener {
        void onRecyclerItemClick(View view, Object data, int position);
    }
    private View.OnClickListener getOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_listener != null && v != null) {
                    m_listener.onRecyclerItemClick(v, billTypes.get(position), position);
                }

            }
        };
    }
    private  int mPosition=-1;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
    private String typeName0;

    public String getTypeName0() {
        return typeName0;
    }

    public void setTypeName0(String typeName0) {
        this.typeName0 = typeName0;
    }
}

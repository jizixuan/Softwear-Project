package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.BillType;

import java.util.ArrayList;
import java.util.List;

public class BillTypeAdapter  extends RecyclerView.Adapter<BillTypeAdapter.ViewHolder>{
    private BillTypeAdapter.OnItemClickListener onItemClickListener;

    private List<BillType> billTypes=new ArrayList<>();

    public BillTypeAdapter(List<BillType> billTypes) {
        this.billTypes = billTypes;
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(BillTypeAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
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
    }

    @Override
    public int getItemCount() {
        return billTypes == null ? 0 : billTypes.size();

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView typeName;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeName=itemView.findViewById(R.id.bill_type_name);
            img=itemView.findViewById(R.id.bill_type_img);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}

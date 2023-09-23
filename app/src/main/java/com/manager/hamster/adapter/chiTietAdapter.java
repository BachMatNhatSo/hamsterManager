package com.manager.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.hamster.R;
import com.manager.hamster.model.item;
import com.manager.hamster.utils.utils;

import java.util.List;

public class chiTietAdapter extends RecyclerView.Adapter<chiTietAdapter.MyViewHolder> {
    Context context;
    List<item> listItem;

    public chiTietAdapter(Context context, List<item> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        item item=listItem.get(position);
        holder.txtten.setText(item.getTensanpham()+" ");
        holder.txtsoluong.setText("Số Lượng: "+item.getSoluong());
        //.with(context).load(item.getHinhanh()).into(holder.imgChiTiet);
        if(item.getHinhanh().contains("http")){
           Glide.with(context).load(item.getHinhanh()).into(holder.imgChiTiet);
        }else {
            String hinh = utils.BASE_URL+"images/"+item.getHinhanh();
            Glide.with(context).load(hinh).into(holder.imgChiTiet);
        }

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class MyViewHolder extends   RecyclerView.ViewHolder{
        ImageView imgChiTiet;
        TextView txtten, txtsoluong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChiTiet=itemView.findViewById(R.id.item_chitietdonhang_img);
            txtten=itemView.findViewById(R.id.item_chitietdonhang_tenSP);
            txtsoluong=itemView.findViewById(R.id.item_chitietdonhang_SoLuongSP);
        }
    }
}

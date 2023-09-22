package com.manager.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.hamster.R;
import com.manager.hamster.model.DonHang;

import java.util.List;

public class donHangAdapter extends RecyclerView.Adapter<donHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool= new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listdonhang;

    public donHangAdapter(Context context, List<DonHang> listdonhang) {
        this.context = context;
        this.listdonhang = listdonhang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang= listdonhang.get(position);
        holder.txtdonhang.setText("Đơn Hàng: "+donHang.getId());
        LinearLayoutManager layoutManager= new LinearLayoutManager(
                holder.rcvChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        //adapter chitiet
        chiTietAdapter chiTietAdapter= new chiTietAdapter(context,donHang.getItem());
        holder.rcvChitiet.setLayoutManager(layoutManager);
        holder.rcvChitiet.setAdapter(chiTietAdapter);
        holder.rcvChitiet.setRecycledViewPool(viewPool);


    }

    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdonhang;
        RecyclerView rcvChitiet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang =itemView.findViewById(R.id.txtIdDonHang);
            rcvChitiet =itemView.findViewById(R.id.rcvChiTietDonHang);
        }
    }
}

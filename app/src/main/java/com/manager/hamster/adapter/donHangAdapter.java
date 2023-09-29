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
        int trangthai = donHang.getTrangthai();
        String trangthaiText = "";

        switch (trangthai) {
            case 0:
                trangthaiText = "Đơn hàng đang chờ xử lý";
                break;
            case 1:
                trangthaiText = "Đơn hàng đã được xác nhận và đang chuẩn bị";
                break;
            case 2:
                trangthaiText = "Đơn hàng đang trên đường vận chuyển";
                break;
            case 3:
                trangthaiText = "Đơn hàng đã bị hủy";
                break;
            case 4:
                trangthaiText = "Giao Thành Công";
                break;
            default:
                trangthaiText = "Không xác định";
                break;
        }
        holder.trangthai.setText(trangthaiText);

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
    private  String trangthaidon(int status){
        String result="";
        switch (status){
            case 0:
                result = "đơn hàng đang chờ";
                break;
            case 1:
                result = "đơn hàng đã xác nhận";
                break;
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdonhang,trangthai;
        RecyclerView rcvChitiet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang =itemView.findViewById(R.id.txtIdDonHang);
            rcvChitiet =itemView.findViewById(R.id.rcvChiTietDonHang);
            trangthai =itemView.findViewById(R.id.txttrangthaidonhang);
        }
    }
}

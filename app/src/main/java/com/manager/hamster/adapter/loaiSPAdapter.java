package com.manager.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.manager.hamster.R;
import com.manager.hamster.model.loaiSP;

import java.util.List;

public class loaiSPAdapter extends BaseAdapter {

    List<loaiSP>  array ;
    Context context;
    public loaiSPAdapter( Context context ,List<loaiSP> array){

        this.context=context;
        this.array=array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        TextView textTenSp;
        ImageView imgHinhAnh;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder =null;
        if(view== null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =layoutInflater.inflate(R.layout.item_sanpham,null);
            viewHolder.textTenSp =view.findViewById(R.id.item_tensp);
            viewHolder.imgHinhAnh =view.findViewById(R.id.item_image);
            view.setTag(viewHolder);
        }else{
            viewHolder =(ViewHolder)view.getTag();

        }viewHolder.textTenSp.setText(array.get(i).getTensanpham());
        Glide.with(context).load(array.get(i).getHinhanh()).into(viewHolder.imgHinhAnh);
        return view;
    }
}

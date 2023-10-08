package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.manager.hamster.R;
import com.manager.hamster.model.gioHang;
import com.manager.hamster.model.sanPhamMoi;
import com.manager.hamster.utils.utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp,giasp,mota;
    Spinner spinner;
    Button btnthemsp;
    ImageView imghinhanh;
    Toolbar toolbar;
    sanPhamMoi sanPhamMoi;
    NotificationBadge notificationBadge;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();

    }

    private void initControl() {
        btnthemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });

    }
    public void themGioHang(){
        if(utils.mangGioHang.size() >0){
            boolean flag =false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i=0 ;i<utils.mangGioHang.size();i++){
                if(utils.mangGioHang.get(i).getIdsp() == sanPhamMoi.getId()){
                    utils.mangGioHang.get(i).setSoluong(soluong + utils.mangGioHang.get(i).getSoluong());
//                    Long gia = Long.parseLong(sanPhamMoi.getGiasp()) * utils.mangGioHang.get(i).getSoluong();
//                    utils.mangGioHang.get(i).setGiasp(gia);
                    flag=true;
                }
            }
            if (flag==false){
                long gia = Long.parseLong(sanPhamMoi.getGiasp());
                gioHang gioHang= new gioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensanpham());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                gioHang.setSltonkho(sanPhamMoi.getSltonkho());
                utils.mangGioHang.add(gioHang);
            }

        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp()) ;
            gioHang gioHang= new gioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensanpham());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            gioHang.setSltonkho(sanPhamMoi.getSltonkho());
            utils.mangGioHang.add(gioHang);
        }
        int totalItem =0 ;
        for (int i=0; i<utils.mangGioHang.size(); i++){
            totalItem = totalItem +utils.mangGioHang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));

    }

    private void initData() {
        sanPhamMoi =(sanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensanpham());
        mota.setText(sanPhamMoi.getMota());
        //Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        if(sanPhamMoi.getHinhanh().contains("http")){

            Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        }else {
            String hinh = utils.BASE_URL+"images/"+sanPhamMoi.getHinhanh();
            Glide.with(getApplicationContext()).load(hinh).into(imghinhanh);
        }
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
//        Integer[] so= new Integer[]{1,2,3,4,5,6,7,8,9,10};
        List<Integer> so = new ArrayList<>();
        for(int i=1; i<sanPhamMoi.getSltonkho()+1;i++){
            so.add(i);
        }
        ArrayAdapter<Integer> adapterSpinner = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterSpinner);
    }

    private void initView() {
        tensp = findViewById(R.id.txttenchitietsp);
        giasp = findViewById(R.id.txtgiachitietsp);
        mota = findViewById(R.id.txtmotachitiet);
        btnthemsp = findViewById(R.id.btnthemvaogiohang);
        spinner =findViewById(R.id.spinnersanphan);
        imghinhanh = findViewById(R.id.imgachitiet);
        toolbar = findViewById(R.id.toolbarManHinhChiTiet);
        notificationBadge = findViewById(R.id.btnCartInChiTiet);
        FrameLayout frameLayout= findViewById(R.id.frameGioHang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(),gioHangActivity.class);
                startActivity(giohang);
            }
        });
        if(utils.mangGioHang != null){
            notificationBadge.setText(String.valueOf(utils.mangGioHang.size()));
        }

    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem =0 ;
        for (int i=0; i<utils.mangGioHang.size(); i++){
            totalItem = totalItem +utils.mangGioHang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }
}
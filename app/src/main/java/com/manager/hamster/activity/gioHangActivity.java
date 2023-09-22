package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.manager.hamster.R;
import com.manager.hamster.adapter.gioHangAdapter;
import com.manager.hamster.model.EventBus.TinhTongEvent;
import com.manager.hamster.model.gioHang;
import com.manager.hamster.utils.utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class gioHangActivity extends AppCompatActivity {
    TextView gioHangTrong, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnXacNhan;
    gioHangAdapter adapterGioHang;
    List<gioHang> gioHangList;
    long tongtiensp  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinhTongTien();
    }

    private void tinhTongTien() {
        tongtiensp =0;
        for(int i=0 ;i<utils.mangMuaHang.size();i++){
            tongtiensp = tongtiensp + (utils.mangMuaHang.get(i).getGiasp() * utils.mangMuaHang.get(i).getSoluong());

        }
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");

        tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(utils.mangGioHang.size() == 0){
            gioHangTrong.setVisibility(View.VISIBLE);
        }else {
            adapterGioHang = new gioHangAdapter(getApplicationContext(),utils.mangGioHang);
            recyclerView.setAdapter(adapterGioHang);
        }
        btnXacNhan=findViewById(R.id.btnXacNhanMuaHang);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGotoThanhToan = new Intent(getApplicationContext(), ThanhToanActivity.class);
                intentGotoThanhToan.putExtra("tongtien",tongtiensp);
                //utils.mangGioHang.clear();
                startActivity(intentGotoThanhToan);
            }
        });

    }

    private void initView() {
        gioHangTrong=findViewById(R.id.txtGioHangRong);
        tongtien=findViewById(R.id.txtTongGiaTien);
        toolbar=findViewById(R.id.toolbarMangHinhGioHang);
        recyclerView=findViewById(R.id.rcvGioHang);
        btnXacNhan=findViewById(R.id.btnXacNhanMuaHang);


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
        public void eventTinhTongTien(TinhTongEvent event){
        if(event != null){
            tinhTongTien();
        }
    }

}
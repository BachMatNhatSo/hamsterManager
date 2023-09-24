package com.manager.hamster.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.manager.hamster.R;
import com.manager.hamster.adapter.sanPhamMoiAdapter;
import com.manager.hamster.model.EventBus.SuaXoaEvent;
import com.manager.hamster.model.sanPhamMoi;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanLyActivity extends AppCompatActivity {

    ImageView imgThemSP_quanly;
    RecyclerView rcvQuanLy;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiHamster apiHamster;
    List<sanPhamMoi> sanPhamMoiList;
    sanPhamMoiAdapter adapter;
    sanPhamMoi sanPhamSuaXoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
         apiHamster = retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        initView();
        initControl();
        getSPMoi();
    }

    private void initControl() {
        imgThemSP_quanly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),ThemSanPhamActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getSPMoi(){
        compositeDisposable.add(apiHamster.getSPMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList =sanPhamMoiModel.getResult();
                                adapter = new sanPhamMoiAdapter(getApplicationContext(),sanPhamMoiList);
                                rcvQuanLy.setAdapter(adapter);
                            }
                        },throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong the ket noi voi sever", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initView() {
        imgThemSP_quanly  = findViewById(R.id.img_QuanLy_ThemSP);
        rcvQuanLy=findViewById(R.id.rcvQuanLy);
        RecyclerView.LayoutManager  layoutManager= new LinearLayoutManager(this);
        rcvQuanLy.setHasFixedSize(true);
        rcvQuanLy.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            suaSanPham();
        }else if(item.getTitle().equals("Xóa")){
            xoaSanPham();
        }
        return super.onContextItemSelected(item);
    }

    private void xoaSanPham() {
        compositeDisposable.add(apiHamster.xoasp(sanPhamSuaXoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    messageModel -> {
                        if(messageModel.isSuccess()){
                            Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                            getSPMoi();
                        }else {
                            Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },throwable -> {
                            Log.d("log",throwable.getMessage());
                        }
                ));
    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
        intent.putExtra("sua",sanPhamSuaXoa);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventSuaXoa(SuaXoaEvent event){
        if(event !=null){
            sanPhamSuaXoa=event.getSanPhamMoi();
        }
    }
    @Override
    protected void onStart() {

        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
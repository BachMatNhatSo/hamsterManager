package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.manager.hamster.R;
import com.manager.hamster.adapter.donHangAdapter;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LichSuDonHangActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiHamster apiHamster;
    RecyclerView rcvDonHang ;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_don_hang);
        initView();
        initControl();
        getDonHang();




    }

    private void getDonHang() {
        apiHamster= retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        compositeDisposable.add(apiHamster.xemdonhang(utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            donHangAdapter adapter = new donHangAdapter(getApplicationContext(),donHangModel.getResult());
                            rcvDonHang.setAdapter(adapter);
                        },throwable -> {

                        }
                ));
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
    }

    private void initView() {
        apiHamster = retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        rcvDonHang= findViewById(R.id.rcvLichSuDonHang);
        toolbar= findViewById(R.id.toolbarManHinhLichSuDonHang);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
         rcvDonHang.setLayoutManager(layoutManager);


    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
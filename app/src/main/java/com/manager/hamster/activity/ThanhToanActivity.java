package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.hamster.R;
import com.manager.hamster.model.gioHang;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien,txtemail,txtphone;
    EditText edittextDiaChi;
    AppCompatButton btnthanhtoan;
    ApiHamster apiHamster;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    long tongtien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();

    }

    private void countItem() {
         totalItem =0 ;
        for (int i=0; i<utils.mangMuaHang.size(); i++){
            totalItem = totalItem +utils.mangMuaHang.get(i).getSoluong();
        }
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
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        tongtien =getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(utils.user_current.getEmail());
        txtphone.setText(utils.user_current.getPhone());

        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi= edittextDiaChi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Ban chua nhap dia chi can giao", Toast.LENGTH_SHORT).show();
                }
                else {
                    //post data
                    String str_email =utils.user_current.getEmail();
                    String str_phone =utils.user_current.getPhone();
                    if(str_email ==null){
                        Toast.makeText(getApplicationContext(), "Bạn cần đăng nhập để thực hiện thanh toán!!!", Toast.LENGTH_SHORT).show();
                    }else{

                        int id =utils.user_current.getId();
                        Log.d("test",new Gson().toJson(utils.mangMuaHang));
                        compositeDisposable.add(apiHamster.createOrder(str_email,str_phone,String.valueOf(tongtien),id,str_diachi,totalItem,new Gson().toJson(utils.mangMuaHang))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        userModel -> {
                                            Toast.makeText(getApplicationContext(), "them don hang thanh cong", Toast.LENGTH_SHORT).show();
                                            for(int i=0 ;i<utils.mangMuaHang.size();i++){
                                                gioHang gioHang = utils.mangMuaHang.get(i);
                                                if(utils.mangGioHang.contains(gioHang)){
                                                    utils.mangGioHang.remove(gioHang);
                                                }
                                            }
                                            utils.mangMuaHang.clear();


                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        },throwable -> {
                                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                ));


                    }

                }
            }
        });
    }

    private void initView() {
        apiHamster = retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        toolbar= findViewById(R.id.toolbarManHinhThanhToan);
        txttongtien = findViewById(R.id.txtTongTienThanhToan);
        txtemail = findViewById(R.id.txtemailThanhToan);
        txtphone = findViewById(R.id.txtPhoneThanhToan);
        edittextDiaChi =findViewById(R.id.edittextDiaChiGiaoHang);
        btnthanhtoan=findViewById(R.id.btnThanhToanDatHang);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
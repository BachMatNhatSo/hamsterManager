package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.hamster.R;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {

    TextView txtemail,txtusername, txtpass,txtrepass,txtphone;
    AppCompatButton btndk;
    ApiHamster apiHamster;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initControl() {
        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = txtemail.getText().toString().trim();
                String str_username = txtusername.getText().toString().trim();
                String str_pass = txtpass.getText().toString().trim();
                String str_repass = txtrepass.getText().toString().trim();
                String str_phone  = txtphone.getText().toString().trim();

                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(str_username)){
                     Toast.makeText(getApplicationContext(), "Bạn chưa nhập Username", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(str_pass)){
                     Toast.makeText(getApplicationContext(), "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(str_repass)){
                     Toast.makeText(getApplicationContext(), "Bạn chưa nhập Re-Password", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(str_phone)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Phone", Toast.LENGTH_SHORT).show();
                }else {
                    if(str_pass.equals(str_repass)){
                        compositeDisposable.add(apiHamster.dangky(str_email,str_pass,str_username,str_phone)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        userModel -> {
                                            if(userModel.isSuccess()){
                                                utils.user_current.setEmail(str_email);
                                                utils.user_current.setEmail(str_pass);
                                                Intent intent = new Intent(DangKyActivity.this,DangNhapActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                  Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        },throwable -> {
                                              Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Pass và Re-pass không khớp!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void dangky() {

    }

    private void initView() {
        apiHamster = retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        txtemail = findViewById(R.id.txtEmail);
        txtusername = findViewById(R.id.txtUsername);
        txtpass = findViewById(R.id.txtPass);
        txtrepass = findViewById(R.id.txtRePass);
        txtphone =findViewById(R.id.txtPhone);
        btndk =findViewById(R.id.btnDangKy);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
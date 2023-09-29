package com.manager.hamster.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.manager.hamster.R;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    Intent intent;
    TextView txtdangky, txtemail,txtpass,txtresetpass;
    Button btndangnhap;
    ApiHamster apiHamster;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    boolean isLogin =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();

    }

    private void initControl() {


        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });
            txtresetpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentresetpass= new Intent(getApplicationContext(), ResetPasswordActivity.class);
                    startActivity(intentresetpass);

                }
            });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = txtemail.getText().toString().trim();
                String str_pass = txtpass.getText().toString().trim();

                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập password", Toast.LENGTH_SHORT).show();
                }else{
                    Paper.book().write("email",str_email);
                    Paper.book().write("password",str_pass);
                    if (user!=null){
                        dangNhap(str_email,str_pass);
                    }else{
                        firebaseAuth.signInWithEmailAndPassword(str_email,str_pass)
                                .addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            dangNhap(str_email,str_pass);
                                        }
                                    }
                                });

                    }


                }
            }
        });

    }
    private void dangNhap(String str_email,String str_pass){


        compositeDisposable.add(apiHamster.dangnhap(str_email,str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                isLogin=true;
                                Paper.book().write("isLogin",isLogin);
                                utils.user_current = userModel.getResult().get(0);
                                Paper.book().write("user",userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        },throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initView() {
        Paper.init(this);
        apiHamster = retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        txtemail=findViewById(R.id.txtEmailDangNhap);
        txtpass= findViewById(R.id.txtPassDangNhap);
        txtdangky= findViewById(R.id.txtDangKy);
        btndangnhap=findViewById(R.id.btnDangNhap);
        txtresetpass= findViewById(R.id.txtQuenMatKhau);
        firebaseAuth =FirebaseAuth.getInstance();
        user =firebaseAuth.getCurrentUser();
        if(Paper.book().read("email") !=null &&Paper.book().read("password") !=null){
            txtemail.setText(Paper.book().read("email"));
            txtpass.setText(Paper.book().read("password"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(utils.user_current.getEmail() != null && utils.user_current.getPassword() !=null){
            txtemail.setText(utils.user_current.getEmail());
            txtpass.setText(utils.user_current.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
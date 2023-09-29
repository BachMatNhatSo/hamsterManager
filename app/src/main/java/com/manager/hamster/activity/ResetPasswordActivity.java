package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.manager.hamster.R;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPasswordActivity extends AppCompatActivity {

    TextView txtemailReset;
    AppCompatButton btnreset;
    ApiHamster apiHamsterl;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
        initControl();
    }

    private void initControl() {
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = txtemailReset.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền email muốn khôi phục mật khẩu!", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(str_email)
                            .addOnCompleteListener(task -> {
                               if(task.isSuccessful()){
                                   Toast.makeText(getApplicationContext(), "Kiểm tra Email của bạn", Toast.LENGTH_SHORT).show();
                               }
                               finish();
                            });
                }
            }
        });
    }

    private void initView() {
        apiHamsterl= retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        txtemailReset=findViewById(R.id.txtemailReset);
        btnreset=findViewById(R.id.btnresetpassword);
        progressBar= findViewById(R.id.progressbarResetPassword);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
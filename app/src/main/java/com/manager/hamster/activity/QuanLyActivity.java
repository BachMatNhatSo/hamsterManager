package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.manager.hamster.R;

public class QuanLyActivity extends AppCompatActivity {

    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        initView();
        initControl();
    }

    private void initControl() {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),ThemSanPhamActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        cardView = findViewById(R.id.CVQuanLy);
    }
}
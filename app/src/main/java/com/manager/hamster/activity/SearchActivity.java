package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.manager.hamster.R;
import com.manager.hamster.adapter.hamsterAdapter;
import com.manager.hamster.model.sanPhamMoi;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rcv;
    hamsterAdapter adapterHamster;
    List<sanPhamMoi> sanPhamMoiList;
    ApiHamster apiHamster;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    EditText editSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();


        ActionToolBar();
    }

    private void initView() {
        sanPhamMoiList= new ArrayList<>();
        apiHamster = retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        toolbar = findViewById(R.id.toolbarManHinhTimKiem);
        rcv =findViewById(R.id.rcvManHinhSearch);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(linearLayoutManager);
        editSearch= findViewById(R.id.txteditSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    sanPhamMoiList.clear();
                    adapterHamster=new hamsterAdapter(getApplicationContext(),sanPhamMoiList);
                    rcv.setAdapter(adapterHamster);

                }
                else{
                    getDataSearch(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getDataSearch(String s) {
        sanPhamMoiList.clear();

        compositeDisposable.add(apiHamster.search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList =sanPhamMoiModel.getResult();
                                adapterHamster=new hamsterAdapter(getApplicationContext(),sanPhamMoiList);
                                rcv.setAdapter(adapterHamster);
                            }else{
                                sanPhamMoiList.clear();
                                adapterHamster.notifyDataSetChanged();
                            }
                        },throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
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
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
package com.manager.hamster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.manager.hamster.R;
import com.manager.hamster.databinding.ActivityThemSanPhamBinding;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThemSanPhamActivity extends AppCompatActivity {

    Spinner spinner;
    int loai =0;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiHamster apiHamster;
    ActivityThemSanPhamBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityThemSanPhamBinding.inflate(getLayoutInflater());
        apiHamster= retrofitClient.getInstance(utils.BASE_URL).create(ApiHamster.class);
        setContentView(binding.getRoot());

        initView();
        initData();
    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn danh mục");
        stringList.add("Hamster Bear");
        stringList.add("Hamster robo");
        stringList.add("Hamster Winter White");
        ArrayAdapter <String> adapter  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnThemSPQuanLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themsanpham();
            }
        });

    }

    private void themsanpham() {
        String string_tensp= binding.txtQuanLyThemTenSanPham.getText().toString().trim();
        String string_giasp= binding.txtQuanLyThemGiaSanPham.getText().toString().trim();
        String string_hinhanhsp= binding.txtQuanLyThemHinhAnhSanPham.getText().toString().trim();
        String string_motasp= binding.txtQuanLyThemMoTaSanPham.getText().toString().trim();

        if(TextUtils.isEmpty(string_tensp)||TextUtils.isEmpty(string_giasp)||TextUtils.isEmpty(string_hinhanhsp)||TextUtils.isEmpty(string_motasp)||loai==0){
            Toast.makeText(getApplicationContext(), "Mời nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiHamster.themsp(string_tensp,string_giasp,string_hinhanhsp,string_motasp,(loai))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                    ));
        }
    }

    private void initView() {
        spinner=findViewById(R.id.spinnerThemSanPhamQuanLy);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
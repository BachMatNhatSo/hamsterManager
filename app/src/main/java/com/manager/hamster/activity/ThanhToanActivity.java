package com.manager.hamster.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.manager.hamster.R;
import com.manager.hamster.model.gioHang;
import com.manager.hamster.retrofit.ApiHamster;
import com.manager.hamster.retrofit.retrofitClient;
import com.manager.hamster.utils.utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtemail, txtphone;
    EditText edittextDiaChi;
    AppCompatButton btnthanhtoan;
    ApiHamster apiHamster;
    ImageView imageview_map;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private ActivityResultLauncher<Intent> locationResultLauncher;
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

        imageview_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra quyền truy cập vị trí của người dùng
                if (ContextCompat.checkSelfPermission(ThanhToanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Nếu không có quyền truy cập vị trí, yêu cầu quyền từ người dùng
                    ActivityCompat.requestPermissions(ThanhToanActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                } else {
                    // Nếu đã có quyền truy cập vị trí, lấy vị trí hiện tại và mở Google Maps
                    getCurrentLocationAndOpenMaps();
                }
            }

        });
        locationResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    // Lấy địa chỉ từ kết quả trả về và đặt vào EditText
                    Intent data = result.getData();
                    if (data != null) {
                        String addressLine = data.getStringExtra("address");
                        edittextDiaChi.setText(addressLine);
                    }
                }
            }
        });

    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_LOCATION_PERMISSION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Nếu người dùng đã cấp quyền truy cập vị trí, lấy vị trí hiện tại và mở Google Maps
                    getCurrentLocationAndOpenMaps();
                } else {
                    Toast.makeText(getApplicationContext(), "Ứng dụng không được cấp quyền truy cập vị trí.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    private void getCurrentLocationAndOpenMaps() {
        // Tạo đối tượng LocationManager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Kiểm tra xem dịch vụ định vị đã được bật hay chưa
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Lấy vị trí hiện tại sử dụng fused location provider
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu quyền truy cập vị trí từ người dùng
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Lấy tọa độ của vị trí hiện tại
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Tạo Intent để mở Google Maps với vị trí hiện tại
                        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        // Kiểm tra xem có ứng dụng Google Maps đã được cài đặt trên thiết bị hay chưa
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            // Mở Google Maps
                            startActivity(mapIntent);
                            locationResultLauncher.launch(mapIntent);

                            // Lấy địa chỉ từ tọa độ và đặt vào EditText
                            Geocoder geocoder = new Geocoder(ThanhToanActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null && addresses.size() > 0) {
                                    Address address = addresses.get(0);
                                    String addressLine = address.getAddressLine(0); // Lấy dòng địa chỉ đầu tiên
                                    edittextDiaChi.setText(addressLine);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Ứng dụng Google Maps không được cài đặt trên thiết bị của bạn.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Không thể lấy vị trí hiện tại.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Hãy bật dịch vụ định vị của bạn.", Toast.LENGTH_SHORT).show();
        }
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
        imageview_map =findViewById(R.id.imgview_Map);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
package com.manager.hamster.retrofit;


import com.manager.hamster.model.DonHangModel;
import com.manager.hamster.model.MessageModel;
import com.manager.hamster.model.UserModel;
import com.manager.hamster.model.loaiSPModel;
import com.manager.hamster.model.sanPhamMoiModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiHamster {
    @GET("getsanpham.php")
    Observable<loaiSPModel> getLoaiSP();

    @GET("getsanphammoi.php")
    Observable<sanPhamMoiModel> getSPMoi();

    @POST("getSpTheoLoai.php")
    @FormUrlEncoded
    Observable<sanPhamMoiModel> getSPTheoLoai(
            @Field("page") int page,
            @Field("loai") int loai
    );
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangky(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("phone") String phone,
            @Field("uid") String uid

    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("password") String password
    );
    @POST("resetpassword.php")
    @FormUrlEncoded
    Observable<UserModel> resetpassword(
            @Field("email") String email

    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createOrder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemdonhang(
            @Field("iduser") int   id

    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<sanPhamMoiModel> search(
            @Field("search") String search

    );
    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> themsp(
            @Field("tensanpham") String tensanpham,
            @Field("giasp") String giasp,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai

    );
    @POST("updatesp.php")
    @FormUrlEncoded
    Observable<MessageModel> suasp(
            @Field("tensanpham") String tensanpham,
            @Field("giasp") String giasp,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai,
            @Field("id") int id

    );
    @POST("xoasp.php")
    @FormUrlEncoded
    Observable<MessageModel> xoasp(
            @Field("id") int id

    );
    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token
    );

    @Multipart
    @POST("upload.php")
    Call <MessageModel>uploadFile(@Part MultipartBody.Part file);

}

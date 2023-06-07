package com.manager.btlonappbanhangonline.retrofit;

import com.manager.btlonappbanhangonline.model.DonHangModel;
import com.manager.btlonappbanhangonline.model.LoaiSpModel;
import com.manager.btlonappbanhangonline.model.MessageModel;
import com.manager.btlonappbanhangonline.model.SanPhamMoiModel;
import com.manager.btlonappbanhangonline.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiBanHang {
    //get data
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();
    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    //post data
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> resetPass(
            @Field("email") String email
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
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
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") int id
    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );
    @POST("xoa.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSanPham(
            @Field("id") int id
    );
    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int id
    );
    @POST("updatesp.php")
    @FormUrlEncoded
    Observable<MessageModel> updatesp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int idloai,
            @Field("id") int id
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(
            @Part MultipartBody.Part file
    );
}

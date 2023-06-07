package com.manager.btlonappbanhangonline.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.btlonappbanhangonline.R;
import com.manager.btlonappbanhangonline.adapter.LoaiSpAdapter;
import com.manager.btlonappbanhangonline.adapter.SanPhamMoiAdapter;
import com.manager.btlonappbanhangonline.model.LoaiSp;
import com.manager.btlonappbanhangonline.model.SanPhamMoi;
import com.manager.btlonappbanhangonline.model.User;
import com.manager.btlonappbanhangonline.retrofit.ApiBanHang;
import com.manager.btlonappbanhangonline.retrofit.RetrofitClient;
import com.manager.btlonappbanhangonline.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if(Paper.book().read("user")!=null){
            User user = Paper.book().read("user");
            Utils.user_current=user;
        }
        Anhxa();
        ActionBar();
        if(isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEnventClick();
        }else{
            Toast.makeText(getApplicationContext(),"Không có internet, vui lòng kết nối!", Toast.LENGTH_LONG).show();
        }
    }

    private void getEnventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                switch (i){
                    case 0:
                        Intent trangchu= new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai= new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai", 1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop= new Intent(getApplicationContext(), DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 3:
                        Intent thongtin= new Intent(getApplicationContext(), ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                    case 4:
                        Intent lienhe= new Intent(getApplicationContext(), LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                    case 5:
                        Intent donhang= new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 6:
                        Intent quanli= new Intent(getApplicationContext(), QuanLiActivity.class);
                        startActivity(quanli);
                        break;
                    case 7:
                    //xoa key user
                    Paper.book().delete("user");
                    Intent dangnhap= new Intent(getApplicationContext(), DangNhapActivity.class);
                    startActivity(dangnhap);
                    finish();
                    break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerViewManHinhChinh.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever"+throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if(loaiSpModel.isSuccess()){
                                mangloaisp = loaiSpModel.getResult();
                                mangloaisp.add(new LoaiSp("Quản lí", "https://icon2.cleanpng.com/20180821/xx/kisspng-computer-icons-vector-graphics-portable-network-gr-admin-svg-png-icon-free-download-537428-onlin-5b7c6d8370c637.3718566315348811554619.jpg"));
                                mangloaisp.add(new LoaiSp("Đăng xuất","data:image/png;base64,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"));
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                                listViewManHinhChinh.setAdapter(loaiSpAdapter);
                            }
                        }
                )
        );
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao=new ArrayList<>();
        mangquangcao.add("https://intphcm.com/data/upload/banner-la-gi.jpg");
        mangquangcao.add("https://insieutoc.vn/wp-content/uploads/2021/02/mau-banner-quang-cao-khuyen-mai.jpg");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i=0;i<mangquangcao.size();i++){
            ImageView imageView=new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setInAnimation(slide_out);
    }

    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
    }
    private void Anhxa(){
        imgsearch= findViewById(R.id.imgsearch);
        toolbar=findViewById(R.id.toobarmanhinhchinh);
        viewFlipper=findViewById(R.id.viewlipper);
        recyclerViewManHinhChinh=findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager= new GridLayoutManager(this,2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh=findViewById(R.id.listviewmanhinhchinh);
        navigationView=findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        badge= findViewById(R.id.menu_sl);
        frameLayout=findViewById(R.id.framegiohang);
        //khoi tao list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if(Utils.manggiohang==null){
            Utils.manggiohang=new ArrayList<>();
        }else{
            int totalItem=0;
            for(int i=0;i<Utils.manggiohang.size();i++){
                totalItem= totalItem+Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang= new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem=0;
        for(int i=0;i<Utils.manggiohang.size();i++){
            totalItem= totalItem+Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}
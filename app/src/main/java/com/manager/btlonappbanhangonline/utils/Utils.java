package com.manager.btlonappbanhangonline.utils;

import com.manager.btlonappbanhangonline.model.GioHang;
import com.manager.btlonappbanhangonline.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.85.112/banhang/";
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static User user_current = new User();
}

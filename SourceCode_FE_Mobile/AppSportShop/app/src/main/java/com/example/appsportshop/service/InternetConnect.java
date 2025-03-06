package com.example.appsportshop.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnect {
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
//            Toast.makeText(context, "Kết Nối Internet Thành công", Toast.LENGTH_LONG).show();
            return true;
        }
//        Toast.makeText(context, "không có kết nối internet !!!", Toast.LENGTH_LONG).show();
        return false;
    }
}

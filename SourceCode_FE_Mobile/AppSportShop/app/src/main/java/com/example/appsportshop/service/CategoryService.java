package com.example.appsportshop.service;


import com.example.appsportshop.R;

import java.util.Arrays;
import java.util.List;
public class CategoryService {



    public static List<String> loadLogoName() {



        String[] nameLogo = new String[]{
                "Giày", "Dép", "Thể Hình", "Bóng Đá", "Võ Thuật", "Phụ Kiện"
        };



        return Arrays.asList(nameLogo);


    }


    public static int[] loadLogo() {
        int[] logo  = new int[]{
                R.drawable.giay,
                R.drawable.dep,
                R.drawable.gym,
                R.drawable.bongda,
                R.drawable.vothuat,
                R.drawable.phukien

        };

        return logo;


    }




}

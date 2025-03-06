package com.example.appsportshop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.fragment.Admin.FragManageEmloyee;
import com.example.appsportshop.fragment.Admin.FragManageCustomer;
import com.example.appsportshop.fragment.Admin.FragManagerProduct;
import com.example.appsportshop.fragment.Admin.FragRevenue;
import com.example.appsportshop.fragment.Customer.FragProfile;
import com.example.appsportshop.fragment.Employee.FragOrder;
import com.example.appsportshop.fragment.Employee.FragSell;
import com.example.appsportshop.utils.SingletonUser;
import com.google.android.material.navigation.NavigationView;

public class MainAdmin extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    ImageView avtShop;
    TextView shopName;


    SingletonUser singletonUser = SingletonUser.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_admin_act);

//        System.out.println(singletonUser.getEmail().equalsIgnoreCase("")+"name1");

//        System.out.println(singletonUser.getEmail()+"name3");
        mapping();
        autoDisplay();



        setEvent();
    }

    private void autoDisplay(){
        if (FragProfile.isDisplay) {
            FragProfile.isDisplay = false;
            FragProfile fragProfile = new FragProfile();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.NoiDung,fragProfile)
                    .commit();
            ;

        } else if (FragManagerProduct.isDisplayManagerProd) {
            FragManagerProduct.isDisplayManagerProd = false;
            FragManagerProduct fragManagerProduct = new FragManagerProduct();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.NoiDung,fragManagerProduct)
                    .commit();
            ;

        } else if (FragManageEmloyee.isDisplay) {
            FragManageEmloyee.isDisplay= false;
            FragManageEmloyee fragManageEmloyee = new FragManageEmloyee();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.NoiDung,fragManageEmloyee)
                    .commit();
            ;

        }
    }

    private void setEvent() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set ảnh đại diện của shop và tên của shop

        Glide.with(MainAdmin.this).load(singletonUser.getAvatarUrl()).into(avtShop);
        shopName.setText(singletonUser.getFullName());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.mn_Profile:
//                        item.setChecked(true);
//                        Toast.makeText(ManagerShop.this, "Quản lí sản phẩm", Toast.LENGTH_SHORT).show();
//                       .setArguments(bundle);
                        FragProfile fragProfile = new FragProfile();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.NoiDung,fragProfile)
                                .commit();
                        ;

                        break;

                    case R.id.mn_emlloyee:
//                        item.setChecked(true);
//                        Toast.makeText(ManagerShop.this, "Quản lí sản phẩm", Toast.LENGTH_SHORT).show();
//                       .setArguments(bundle);
                        FragManageEmloyee fragDiscout = new FragManageEmloyee();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.NoiDung,fragDiscout)
                                .commit();
                        ;

                        break;

                    case R.id.mn_customer:
//                        item.setChecked(true);
//                        Toast.makeText(ManagerShop.this, "Quản lí sản phẩm", Toast.LENGTH_SHORT).show();
//                       .setArguments(bundle);
                        FragManageCustomer fragManagerCategory = new FragManageCustomer();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.NoiDung,fragManagerCategory)
                                .commit();
                        ;

                        break;

                    case R.id.mn_ManagerPD:
//                        item.setChecked(true);
//                        Toast.makeText(ManagerShop.this, "Quản lí sản phẩm", Toast.LENGTH_SHORT).show();
//                        fragManagerProduct.setArguments(bundle);
                        FragManagerProduct fragManagerProduct = new FragManagerProduct();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.NoiDung,fragManagerProduct)
                                .commit();
                        ;

                        break;
                    case R.id.mn_Revenue:
//                        item.setChecked(true);
//                        Toast.makeText(ManagerShop.this, "Thống Kê Doanh Thu", Toast.LENGTH_SHORT).show();
                        FragRevenue fragRevenue = new FragRevenue();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.NoiDung,fragRevenue)
                                .commit();

                        break;

                    case R.id.mn_ManagerOrder:
                        FragOrder fragOrder = new FragOrder();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.NoiDung,fragOrder)
                                .commit();

                        break;
//
//
                    case R.id.mn_Sell:
                        FragSell fragSell = new FragSell();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.NoiDung,fragSell)
                                .commit();

                        break;
//
//
                    case R.id.mn_Exit:
                        item.setChecked(true);
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("matkhau", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();
                        singletonUser.clearValues();
//                        Toast.makeText(ManagerShop.this, "Cài đặt", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        break;

                }
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    private void mapping() {

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationview);
        View headerView = navigationView.getHeaderView(0);
        avtShop =  headerView.findViewById(R.id.avt_Shop);
        shopName = (TextView) headerView.findViewById(R.id.shopName);
    }
}

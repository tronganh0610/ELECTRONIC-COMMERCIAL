package com.example.appsportshop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.fragment.Admin.FragManagerProduct;
import com.example.appsportshop.fragment.Admin.FragRevenue;
import com.example.appsportshop.fragment.Customer.FragProfile;
import com.example.appsportshop.fragment.Employee.FragOrder;
import com.example.appsportshop.fragment.Employee.FragSell;
import com.example.appsportshop.utils.SingletonUser;
import com.google.android.material.navigation.NavigationView;

public class MainEmployee extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    ImageView avtShop;
    TextView shopName;
    SingletonUser singletonUser = SingletonUser.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        setContentView(R.layout.main_employee);

        mapping();

        FragOrder fragOrder = new FragOrder();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.NoiDung,fragOrder)
                .commit();


        setEvent();

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

    private void setEvent() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(MainEmployee.this).load(singletonUser.getAvatarUrl()).into(avtShop);
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

}

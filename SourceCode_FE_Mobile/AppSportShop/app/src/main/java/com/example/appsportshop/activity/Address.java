package com.example.appsportshop.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appsportshop.R;
import com.example.appsportshop.model.Cart;
import com.example.appsportshop.utils.SingletonUser;


import java.util.ArrayList;

public class Address extends AppCompatActivity {
    EditText name, sdt, sonha, district;
    TextView btnSubmitAddress;
    ArrayList<Cart> listProductPayment;
    String idUser="";
    String tongTien="";
    public  static  Boolean isDisplay = false;
    SingletonUser singletonUser = SingletonUser.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_address_method);
//        listProductPayment = getIntent().getExtras().getParcelableArrayList("listOrder");
        //lấy iduser được gửi từ FragCart
//        Intent intent = getIntent();
//        idUser = getIntent().getStringExtra("idUser");
//        tongTien = getIntent().getStringExtra("tongTien");
        mapping();
        getnewAddress();

    }
    private void mapping(){
        name = findViewById(R.id.NameShipping);
        name.setText(singletonUser.getFullName());
        sdt = findViewById(R.id.sodienthoai);
        sdt.setText(singletonUser.getPhone());
        sonha= findViewById(R.id.sonhatenduong);
        district = findViewById(R.id.huyenthanhpho);
        btnSubmitAddress =findViewById(R.id.btnSubmit1);
    }
    private void getnewAddress(){
        btnSubmitAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameShip = (name.getText().toString());
                String phoneNumber = (sdt.getText().toString());
                String homeNumber =(sonha.getText().toString());
                String districtName =(district.getText().toString());
                tongTien =(district.getText().toString());
                if(TextUtils.isEmpty((nameShip))){
                    name.setError("Vui lòng không để trống");
                }
                if(TextUtils.isEmpty((phoneNumber))){
                    sdt.setError("Vui lòng không để trống");
                }
                if(TextUtils.isEmpty((homeNumber))){
                    sonha.setError("Vui lòng không để trống");
                }
                if(TextUtils.isEmpty((districtName))){
                    district.setError("Vui lòng không để trống");
                }
                if(!TextUtils.isEmpty((nameShip))&&!TextUtils.isEmpty((phoneNumber))&&!TextUtils.isEmpty((homeNumber))&&!TextUtils.isEmpty((districtName))){
//                    System.out.println(nameShip);
//                    System.out.println(phoneNumber);
//                    System.out.println(homeNumber);
//                    System.out.println(districtName);
                    isDisplay= true;
                    Intent intent = new Intent(Address.this, Payment.class);
//                    intent.putExtra("listOrder", listProductPayment);
//                    intent.putExtra("idUser",idUser);
                    tongTien = getIntent().getStringExtra("tongTien");

                    intent.putExtra("name_reciver", nameShip);
                    intent.putExtra("phoneNumber",phoneNumber);
                    intent.putExtra("ship_adress", homeNumber+" "+districtName);
                    intent.putExtra("tongTien",tongTien);
                    startActivity(intent);
                }
            }
        });
    }
}
package com.example.appsportshop.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import com.example.appsportshop.R;
import com.example.appsportshop.adapter.PayMentAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.CartAPI;
import com.example.appsportshop.api.OrderAPI;
import com.example.appsportshop.fragment.Customer.FragCart;
import com.example.appsportshop.model.Cart;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPaySDK;*/

public class Payment extends AppCompatActivity {

    BottomSheetDialog dialogPayment;
    TextView btnMethodPayment ,sdtName, btnMuaHang, ship_adree, tongThanhtoan, tongthanhtoan1, tongthanhtoan2; //soNha+thanhPho = address
    LinearLayout btnAddress;
    ImageView btnbackPayment;

    Boolean isShipcode =null;



    public static Boolean  isActive = false;
    ArrayList<Cart> listProductPayment;
    ListView lvPayment;
    String idUser="";
    String nameShip="";
    String phoneNumber="";
    String homeNumber="";
    String districtName="";
    String tongTien="";

    public static String diaChiShip;
    public static String sdtNgNhan;
    public static String tenNgNhan;

    SingletonUser singletonUser = SingletonUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);


        mapping();
        dialogPayment = new BottomSheetDialog(this);
        createDialog();

//        System.out.println(Address.isDisplay+"huhuuh");
        if (Address.isDisplay == true) {

            Address.isDisplay = false;

            nameShip = getIntent().getStringExtra("name_reciver");
            phoneNumber = getIntent().getStringExtra("phoneNumber");
            districtName = getIntent().getStringExtra("ship_adress");
            sdtName.setText(nameShip+"| (+84) "+phoneNumber);
            tongTien = getIntent().getStringExtra("tongTien");
            ship_adree.setText(districtName);
        } else {
            idUser = String.valueOf(singletonUser.getIdUser());
            nameShip = singletonUser.getFullName();
            phoneNumber = singletonUser.getPhone();
            districtName = singletonUser.getAdress();
            sdtName.setText(nameShip+"| (+84) "+phoneNumber);
            ship_adree.setText(districtName);
            tongTien =getIntent().getStringExtra("tongTien");

        }


//        getList những sản phẩm được mua form FragCart
        listProductPayment = new ArrayList<>();
        for (int i = 0; i < FragCart.listCart.size(); i++) {
            if (FragCart.listCart.get(i).getSelected()) {
                listProductPayment.add(FragCart.listCart.get(i));
            }
        }

//        listProductPayment= FragCart.listCart;

//        listProductPayment = getIntent().getExtras().getParcelableArrayList("listOrder");

        //lấy iduser được gửi từ FragCart
//        Intent intent = getIntent();

        tongThanhtoan.setText(tongTien);
        tongthanhtoan1.setText(tongTien);
        tongthanhtoan2.setText(tongTien);

//        if(!TextUtils.isEmpty(nameShip)&&!TextUtils.isEmpty(phoneNumber)&&!TextUtils.isEmpty(homeNumber)&&!TextUtils.isEmpty(districtName)) {
//            sdtName.setText(nameShip+"| (+84) "+phoneNumber);
//            soNha.setText(homeNumber);
//            thanhPho.setText(districtName);
//        }

//        ListCartDaMua();
        newAddress();
        setEvent();






    }


    private void newAddress(){
        btnAddress.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Payment.this, Address.class);
//                intent.putExtra("listOrder",listProductPayment);
//                intent.putExtra("idUser",idUser);
                intent.putExtra("tongTien",tongTien);
                startActivity(intent);
            }
        });
    }
    private void setEvent() {


        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isShipcode == null) {
                    CustomToast.makeText(Payment.this, "Vui lòng chọn phương thức thanh toán !", CustomToast.LENGTH_SHORT, CustomToast.WARNING, true).show();

                }else if (isShipcode){
                    try {
                        APIMuaHang();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {

                    Intent intent = new Intent(Payment.this, ZaloPay.class);
                    System.out.println(Payment.tenNgNhan+"nam");

                    int spaceIndex = tongThanhtoan.getText().toString().indexOf(' ');

                    String amountStr = tongThanhtoan.getText().toString().substring(0, spaceIndex); // Lấy phần số từ đầu chuỗi đến vị trí khoảng trắng

                    System.out.println(amountStr+"++++++++++++");
                    intent.putExtra("tongtien", amountStr);
                    startActivity(intent);

//                    Intent intent1= new Intent(Payment.this, ZaloPay.class);
//                    startActivity(intent1);
                }

            }
        });


        btnMethodPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPayment.show();
            }
        });

        PayMentAdapter payMentAdapter = new PayMentAdapter(getApplicationContext(),R.layout.item_payment, listProductPayment);
        lvPayment.setAdapter(payMentAdapter);

        btnbackPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isActive = true;
                startActivity(new Intent(getApplicationContext(), Main_Customer.class));
            }
        });
    }

    public  void APIMuaHang() throws JSONException {

        String idQuantities ="";
        String idProducts ="";

        for (int i = 0; i < listProductPayment.size(); i++) {
            idQuantities += listProductPayment.get(i).getQuantity()+",";
            idProducts+=listProductPayment.get(i).getIdProduct()+",";
        }
        idQuantities =  idQuantities.replaceAll(",$", "");
        idProducts =  idProducts.replaceAll(",$", "");

        OrderAPI.BuyProduct(getApplicationContext(),
                Utils.BASE_URL + "order/buy", singletonUser.getIdUser(),
                1, String.valueOf(ship_adree.getText()), idProducts, idQuantities, phoneNumber, nameShip, new APICallBack() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                        RemoveProductInCart();
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    private void RemoveProductInCart() throws JSONException {

        try {
            for (int i = 0; i < FragCart.listCart.size(); i++) {

                for (int j = 0; j < listProductPayment.size(); j++) {

                    if (FragCart.listCart.get(i).getId() == listProductPayment.get(i).getId()){
//                        FragCart.listCart.remove(i);
//                        if (FragCart.listCart.size()==0) return;
                        CartAPI.DeleteCart(getApplicationContext(), Utils.BASE_URL + "cart-management/" + FragCart.listCart.get(i).getId(), new APICallBack() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {

                                    CustomToast.makeText(Payment.this,  "     Mua hàng thành công ! \n Cảm ơn bạn đã tin tưởng Shop", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();

                                    Intent intent = new Intent(getApplicationContext(), OrderItem.class);
                                    startActivity(intent);


                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });
                    }
                }
            }
        }catch (Exception e) {
            CustomToast.makeText(Payment.this,  "     Mua hàng thành công ! \n Cảm ơn bạn đã tin tưởng Shop", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();

            Intent intent = new Intent(getApplicationContext(), OrderItem.class);
            startActivity(intent);
        }



    }

    private  void mapping(){
        btnMethodPayment = findViewById(R.id.shipMethod);
        btnbackPayment = findViewById(R.id.ic_backPayment);
        btnAddress =findViewById(R.id.btnAddress);
        lvPayment = findViewById(R.id.lvPayment);
        btnMuaHang = findViewById(R.id.btnPayment);
        ship_adree = findViewById(R.id.soNha);
        sdtName =findViewById(R.id.sdtName);
        tongThanhtoan= findViewById(R.id.tongthanhtoan);
        tongthanhtoan1= findViewById(R.id.tongthanhtoan1);
        tongthanhtoan2= findViewById(R.id.tongthanhtoan2);

//        tongTien = findViewById(R.id.tienhang);
//        tongThanhToan = findViewById(R.id.tong);

    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.method_payment, null, false);
        TextView btnSubmit = view.findViewById(R.id.btnPaymentmethod);
        String displayText = "Xác nhận thanh toán";
        SpannableString spannableString = new SpannableString(displayText);

        CheckBox shipcode = view.findViewById(R.id.shipcode);

        CheckBox zalo = view.findViewById(R.id.zalo);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zalo.isChecked()){
                    isShipcode=false;
                    shipcode.setChecked(false);
                }
                if (shipcode.isChecked()){
                    isShipcode=true;
                    zalo.setChecked(false);
                }
                dialogPayment.dismiss();
                System.out.println(isShipcode);
            }
        });

//        EditText edtQuanti = findViewById();

        // Tạo ClickableSpan để xử lý việc click vào liên kết

        dialogPayment.setContentView(view);






    }


}

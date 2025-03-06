package com.example.appsportshop.fragment.Customer;
// cuối kỳ

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.Address;
import com.example.appsportshop.activity.Login;
import com.example.appsportshop.activity.Main_Customer;
//import com.example.appsportshop.activity.Payment;
import com.example.appsportshop.activity.Payment;
import com.example.appsportshop.adapter.CartAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.CartAPI;
import com.example.appsportshop.model.Cart;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FragCart extends Fragment implements Serializable {

    public static ArrayList<Cart> listCart;
    long idUser ; //ttesstt  63af70c03f562b7531d4c5db
    TextView btnBuyCart;
    LinearLayout exsit;
    public static TextView tongTien;
    CartAdapter cartAdapter;
    ImageView notCart;

    ListView listViewCart;

    public static Boolean isDisplay = false;

    public static CheckBox cbCheckAll;

    private OnBackPressedCallback callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Không thực hiện gì cả để vô hiệu hóa nút "Back" mặc định
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Bỏ điều kiện ghi đè khi Fragment bị hủy
        callback.remove();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_cart, container, false);
        //lấy dữ liệu đc gửi từ trang Main

         SingletonUser singletonUser = SingletonUser.getInstance();
        idUser = singletonUser.getIdUser();

        mapping(view);
        if (singletonUser.getToken()==null) {
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        }else {
            try {
                CartAPI.getCartByUser(getContext(), Utils.BASE_URL + "cart-management/carts?idUser="+idUser, new APICallBack() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
//                    System.out.println("api get cart by user: "+response);
                        JSONArray listcartJSON = response.getJSONArray("data");
                        listCart = new ArrayList<>();
                        for (int i = 0; i < listcartJSON.length(); i++) {
                            JSONObject cartTmpObj = (JSONObject) listcartJSON.get(i);
//

                            Cart cartTemp = new Cart();
                            cartTemp.setId(cartTmpObj.getLong("id"));
                            cartTemp.setQuantity(cartTmpObj.getInt("quantity"));
                            cartTemp.setPrice_total(cartTmpObj.getLong("price_total"));
                            cartTemp.setUrlImage(cartTmpObj.getString("image_url"));
                            cartTemp.setNameProduct(cartTmpObj.getString("product_name"));
                            cartTemp.setIdProduct(cartTmpObj.getString("product_id"));


                            listCart.add(cartTemp);

                        }
                        setEvent();

                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }





        return view;
    }
    private void mapping(View view) {
        listViewCart = view.findViewById(R.id.listViewCart);
        btnBuyCart = view.findViewById(R.id.btnBuyCart);
        cbCheckAll = view.findViewById(R.id.selectAll);
        tongTien = view.findViewById(R.id.tongtien);
        exsit = view.findViewById(R.id.exitCart);
        notCart = view.findViewById(R.id.notCart);


    }

    private void setEvent() {
        exsit.setVisibility(View.GONE);
        Glide.with(getContext()).load("https://res.cloudinary.com/dtjdyvzob/image/upload/v1717382945/tronganh/avatar/itf8iusk58nikqvmfryr.jpg").into(notCart);

        if (listCart.size()==0){
            exsit.setVisibility(View.VISIBLE);
        }


        cartAdapter = new CartAdapter(getContext(), R.layout.row_cart, listCart);
        listViewCart.setAdapter(cartAdapter);





//click mua hang
        btnBuyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Address.isDisplay = false;
                Intent intent = new Intent(getContext(), Payment.class);
                intent.putExtra("listOrder", buyCarts());
//                intent.putExtra("idUser",idUser);
                intent.putExtra("tongTien",tongTien.getText().toString());
                startActivity(intent);
            }
        });

//click chon tat ca
        cbCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartAdapter.notifyDataSetChanged();
                if (cbCheckAll.isChecked()) {
                    cartAdapter.CheckAll();
                } else if (!cbCheckAll.isChecked()) {
                    cartAdapter.UnCheckAll();
                }

            }
        });

        listViewCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                cartAdapter = new CartAdapter(getContext(), R.layout.row_cart, listCart);
//                listViewCart.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();

            }
        });


    }

    private ArrayList<Cart> buyCarts() {
        ArrayList<Cart> BuyCartList = new ArrayList<>();
        for (int i = 0; i < listCart.size(); i++) {
            if (listCart.get(i).getSelected()) {
                BuyCartList.add(listCart.get(i));
//                System.out.println(BuyCartList.get(i).getId()+"|||000000000000000000");

            }
        }
        return BuyCartList;
    }



}
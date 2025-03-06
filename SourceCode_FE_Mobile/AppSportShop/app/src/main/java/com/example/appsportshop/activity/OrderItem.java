package com.example.appsportshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.adapter.CartAdapter;
import com.example.appsportshop.adapter.OrderAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.OrderAPI;
import com.example.appsportshop.fragment.Customer.FragHome;
import com.example.appsportshop.model.Cart;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderItem    extends AppCompatActivity {

public  static boolean isChose = false;
    public  ArrayList<Cart> orderItemList;
    ListView listViewOrder;
    OrderAdapter orderAdapter;

    ImageView btnReturn, notItemOrder;

    LinearLayout exitOrder;

    SingletonUser singletonUser = SingletonUser.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avt_orderitem);

        mapping();
        try {
            getOrderItemByIdUser();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        setEvent();


    }

    private void setEvent() {


        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String idProduct = orderItemList.get(i).getIdProduct();
                Intent intent = new Intent(OrderItem.this, ProductDetail.class);

                intent.putExtra("idProduct",idProduct);
                OrderItem.isChose = true;
//                intent.putExtra("tongTien",tongTien.getText().toString());
                startActivity(intent);

            }
        });


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragHome.isDispHomeCustommer= true;
                startActivity(new Intent(OrderItem.this, Main_Customer.class));
            }
        });
    }

    private void mapping() {
        listViewOrder = findViewById(R.id.listViewOrder);
        exitOrder = findViewById(R.id.exitOrder);
        btnReturn = findViewById(R.id.returnOrder);
        notItemOrder = findViewById(R.id.notItemOrder);

    }

    private void getOrderItemByIdUser() throws JSONException {

        OrderAPI.getOrderItemByUser(OrderItem.this, Utils.BASE_URL + "order/by-user/" + singletonUser.getIdUser(), new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONArray listcartJSON = response.getJSONArray("data");
                orderItemList = new ArrayList<>();
                for (int i = 0; i < listcartJSON.length(); i++) {
                    JSONObject cartTmpObj = (JSONObject) listcartJSON.get(i);
//

                    Cart cartTemp = new Cart();
                    cartTemp.setId(cartTmpObj.getLong("id_order"));
                    cartTemp.setQuantity(cartTmpObj.getInt("quantity"));
                    cartTemp.setPrice_total(cartTmpObj.getLong("price"));
                    cartTemp.setUrlImage(cartTmpObj.getString("image_url"));
                    cartTemp.setNameProduct(cartTmpObj.getString("product_name"));
                    cartTemp.setIdProduct(cartTmpObj.getString("id_product"));
                    cartTemp.setId_order_status(cartTmpObj.getLong("id_order_status"));
                    orderItemList.add(cartTemp);

                }
                exitOrder.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load("https://res.cloudinary.com/dtjdyvzob/image/upload/v1717383121/tronganh/avatar/jkqs4j4phut4y90a4e5d.jpg").into(notItemOrder);

                if (orderItemList.size()==0){
                    exitOrder.setVisibility(View.VISIBLE);
                }
                orderAdapter = new OrderAdapter(OrderItem.this, R.layout.row_order, orderItemList);
                listViewOrder.setAdapter(orderAdapter);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}

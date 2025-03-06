package com.example.appsportshop.fragment.Admin;
// cuối kỳ

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.ManagerProductDetail;
import com.example.appsportshop.adapter.ProductManagerAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.ProductAPI;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.model.Product;
import com.example.appsportshop.model.User;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragManagerProduct extends Fragment {

    JSONArray listProduct = new JSONArray();
    ListView listViewProduct;
    ArrayList<Product> ProductList ;

    private ProductManagerAdapter productManagerAdapter;

    Button btnAddProduct;



    SingletonUser singletonUser = SingletonUser.getInstance();

    public static Boolean isDisplayManagerProd = false;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.layout_manager_product, container, false);



//        initData();
        mapping(view);


        try {
            ProductAPI.getAllProduct(getContext(), Utils.BASE_URL + "product/allProduct", new APICallBack() {

                @Override
                public void onSuccess(JSONObject response) throws JSONException {
                    listProduct = response.getJSONArray("data");
                    JSONObject productObj = new JSONObject();
                    Product productTmp;
                    ProductList= new ArrayList<>();
                    for (int i = 0; i < listProduct.length(); i++) {
                        productObj = (JSONObject) listProduct.get(i);
                        productTmp = new Product();
                        productTmp.setId(productObj.getLong("id"));
                        productTmp.setNameProduct(productObj.getString("productName"));
                        productTmp.setStockQuantity(productObj.getInt("stockQuantity"));
                        productTmp.setPrice(Float.parseFloat(productObj.getString("price")));
                        productTmp.setDescription(productObj.getString("description"));
                        productTmp.setUrlImage(productObj.getString("imageUrl"));
                        productTmp.setPublicId(productObj.getString("publicId"));

                        ProductList.add(productTmp);

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

        return view;
    }

    private void setEvent() {
        productManagerAdapter = new ProductManagerAdapter(getContext(), R.layout.row_manager_product1, ProductList);
        listViewProduct.setAdapter(productManagerAdapter);


//        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                positionCurrent = i;
//                product = ProductList.get(i);
//            }
//        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManagerProductDetail.class);
                startActivity(intent);


            }
        });


    }

    private void mapping(View view) {
        listViewProduct = view.findViewById(R.id.listviewManagerProduct);

        btnAddProduct = view.findViewById(R.id.addProduct);

    }

}

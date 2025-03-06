package com.example.appsportshop.fragment.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.ProductDetail;
import com.example.appsportshop.adapter.ProductLatestAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.ProductAPI;
import com.example.appsportshop.utils.ObjectWrapperForBinder;
import com.example.appsportshop.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragSearch extends Fragment {

    GridView gr_productList;
    JSONArray listProduct = new JSONArray();
    List<String> nameProductList = new ArrayList<String>();

    List<Double> priceProductList = new ArrayList<Double>();

    Button bestsell,btnLatest;

    List<String> urlProductList = new ArrayList<String>();
    ProductLatestAdapter latestProduct_test;
    EditText edtSearch;
    private void Mapping(View view) {
        gr_productList = view.findViewById(R.id.search_list);
        edtSearch = view.findViewById(R.id.edtsearch_product);
        bestsell = view.findViewById(R.id.bestsell);
        btnLatest = view.findViewById(R.id.latest);

    }

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
        View view = inflater.inflate(R.layout.frag_search, container, false);
        //lấy dữ liệu đc gửi từ trang Main
        Mapping(view);
        nameProductList.clear();
        priceProductList.clear();
        urlProductList.clear();

        try {
            getAllProduct();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return view;

    }

    private void getAllProduct() throws JSONException {
        ProductAPI.GetProductByCategory(getContext(), Utils.BASE_URL + "product/byText?text="+edtSearch.getText(), new APICallBack() {

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    nameProductList.clear();
                    priceProductList.clear();
                    urlProductList.clear();

                    listProduct = response.getJSONArray("data");

                    JSONObject productTmp;
                    String urlImgTmp = "";
                    for (int i = 0; i < listProduct.length(); i++) {




                        productTmp = (JSONObject) listProduct.get(i);
                        nameProductList.add((String) productTmp.get("productName"));
                        urlImgTmp = (String) productTmp.get("imageUrl");
                        priceProductList.add((productTmp.getDouble("price")));
                        urlProductList.add(urlImgTmp);
                        //                                System.out.println(urlImgTmp);

                    }
                    setAdapter();
                    setEvent();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }


    private void setAdapter() {
        latestProduct_test = new ProductLatestAdapter(getContext(), nameProductList, priceProductList, urlProductList);
        gr_productList.setAdapter(latestProduct_test);
    }
    private void setEvent() {
        //click item product
        gr_productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = "";
                try {
                    final Object objReceived = listProduct.get(i);
                    JSONObject productObj = (JSONObject) objReceived;
                    id = productObj.getString("id");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(getContext(), ProductDetail.class);


                intent.putExtra("idProduct", id);

                startActivity(intent);
            }
        });

        btnLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getAllProduct();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        bestsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ProductAPI.GetProductByCategory(getContext(), Utils.BASE_URL + "product/best_sell", new APICallBack() {

                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                nameProductList.clear();
                                priceProductList.clear();
                                urlProductList.clear();

                                listProduct = response.getJSONArray("data");

                                JSONObject productTmp;
                                String urlImgTmp = "";
                                for (int i = 0; i < listProduct.length(); i++) {

                                    productTmp = (JSONObject) listProduct.get(i);
                                    nameProductList.add((String) productTmp.get("productName"));
                                    urlImgTmp = (String) productTmp.get("imageUrl");
                                    priceProductList.add((productTmp.getDouble("price")));
                                    urlProductList.add(urlImgTmp);
                                    //                                System.out.println(urlImgTmp);

                                }
                                setAdapter();
                                setEvent();

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nameProductList.clear();
                    priceProductList.clear();
                    urlProductList.clear();
                    ProductAPI.GetProductByCategory(getContext(), Utils.BASE_URL + "product/byText?text="+edtSearch.getText(), new APICallBack() {

                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                nameProductList.clear();
                                priceProductList.clear();
                                urlProductList.clear();

                                listProduct = response.getJSONArray("data");

                                JSONObject productTmp;
                                String urlImgTmp = "";
                                for (int i = 0; i < listProduct.length(); i++) {




                                    productTmp = (JSONObject) listProduct.get(i);
                                    nameProductList.add((String) productTmp.get("productName"));
                                    urlImgTmp = (String) productTmp.get("imageUrl");
                                    priceProductList.add((productTmp.getDouble("price")));
                                    urlProductList.add(urlImgTmp);
                                    //                                System.out.println(urlImgTmp);

                                }
                                setAdapter();
                                setEvent();

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
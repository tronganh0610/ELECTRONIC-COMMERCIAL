package com.example.appsportshop.fragment.Employee;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.adapter.ItemOrderAdapter;
import com.example.appsportshop.adapter.OrderAdminAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.model.HoaDon;
import com.example.appsportshop.model.Order;
import com.example.appsportshop.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragOrder extends Fragment {
    private Order orderUser;
    private ArrayList<Order> listOrder;
    List<HoaDon> hoaDonList;
    ListView listViewOrder;
    OrderAdminAdapter orderAdminAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_manager_order_shop, container, false);
        //lấy dữ liệu đc gửi từ trang ManagerShop

        mapping(view);

        try {
            getListOrderConfimed();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return view;
    }

    private void getListOrderConfimed() throws JSONException {
        UserAPI.ApiGet(getContext(), Utils.BASE_URL+"order/done-confirm", new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONArray dataArr = (JSONArray) response.get("data");
                JSONObject data; //item data in dataArr

                listOrder = new ArrayList<>();
                for (int i = 0; i < dataArr.length(); i++) {
                    Order orderTmp = new Order();
                    data = (JSONObject) dataArr.get(i);
                        orderTmp.setId(data.getString("id"));
                        orderTmp.setOrderDate(data.getString("orderDate"));
                    orderTmp.setTotalAmount(Float.parseFloat(data.getString("totalAmount")));
                        orderTmp.setName_ceciver(data.getString("name_reciver"));
                        orderTmp.setShippingAdress(data.getString("shippingAdress"));
//                    System.out.println(data.getString("shippingAdress") + "123456 ");
                        orderTmp.setPhoneNumber(data.getString("sdt"));
                        JSONObject tmp= (JSONObject) data.get("orderStatus");
                        orderTmp.setIdOderStatus(tmp.getString("orderStatusName"));


                    listOrder.add(orderTmp);

                }
//                System.out.println("================");
//                System.out.println(listOrder.get(0).getId());
//                System.out.println(listOrder.get(1).getId());
//                System.out.println("================");
                setEvent();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


    private void mapping(View view) {
        listViewOrder = view.findViewById(R.id.listviewManagerOrder);

    }

    private void setEvent() {
        FragSell.isSell= false;
        orderAdminAdapter = new OrderAdminAdapter(getContext(), R.layout.row_manager_order, listOrder);
//        System.out.println(orderAdapter);
        listViewOrder.setAdapter(orderAdminAdapter);


        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Order
                try {
                    popup(listOrder.get(i).getId());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void popup(String idOrder) throws JSONException {

        Dialog dialog = new Dialog(getContext());
        //
        UserAPI.ApiGet(getContext(), Utils.BASE_URL + "order/export-bill/"+idOrder, new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONArray jsonArray = response.getJSONArray("data");
                HoaDon hoaDon;
                hoaDonList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tmp = (JSONObject) jsonArray.get(i);
                    hoaDon = new HoaDon();
                    hoaDon.setId_Order(tmp.getString("id_Order"));
                    hoaDon.setPrice(Float.parseFloat(tmp.getString("price")));
                    hoaDon.setShippingAdress(tmp.getString("shipping_adress"));
                    hoaDon.setSdt(tmp.getString("sdt"));
                    hoaDon.setNameReciver(tmp.getString("name_reciver"));
                    hoaDon.setProductName(tmp.getString("product_name"));
                    hoaDon.setQuantity(Long.parseLong(tmp.getString("quantity")));
                    hoaDonList.add(hoaDon);
                }
                dialog.setContentView(R.layout.order_item_list);
                ListView listView = dialog.findViewById(R.id.listview_itemorder);
                ItemOrderAdapter orderAdminAdapter = new ItemOrderAdapter(getContext(), R.layout.row_item_order, (ArrayList<HoaDon>) hoaDonList);
                listView.setAdapter(orderAdminAdapter);

                Button confirm = dialog.findViewById(R.id.confirm);
                confirm.setText("THOÁT");

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

}

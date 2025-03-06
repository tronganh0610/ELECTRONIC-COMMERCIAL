package com.example.appsportshop.fragment.Employee;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.Login;
import com.example.appsportshop.activity.MainAdmin;
import com.example.appsportshop.activity.OrderItem;
import com.example.appsportshop.adapter.ItemOrderAdapter;
import com.example.appsportshop.adapter.OrderAdminAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.model.HoaDon;
import com.example.appsportshop.model.Order;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.PdfExporter;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragSell extends Fragment {

    private ArrayList<Order> listOrder;
    SingletonUser singletonUser = SingletonUser.getInstance();
    ListView listViewOrder;
    OrderAdminAdapter orderAdminAdapter;

    List<HoaDon> hoaDonList;
    public static Boolean isSell = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_manager_sell, container, false);
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
        UserAPI.ApiGet(getContext(), Utils.BASE_URL + "order/wait-confirm", new APICallBack() {
            @SuppressLint("SuspiciousIndentation")
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
                    if (data.getString("totalAmount").equalsIgnoreCase("null"))
                        orderTmp.setTotalAmount(0);
                    else
                        orderTmp.setTotalAmount(Float.parseFloat(data.getString("totalAmount")));
                    orderTmp.setName_ceciver(data.getString("name_reciver"));
                    System.out.println(data.getString("shippingAdress")+"0101");
                    orderTmp.setShippingAdress(data.getString("shippingAdress"));
                    orderTmp.setPhoneNumber(data.getString("sdt"));
                    JSONObject tmp = (JSONObject) data.get("orderStatus");
                    orderTmp.setIdOderStatus(tmp.getString("orderStatusName"));


                    listOrder.add(orderTmp);

                }

                setEvent();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


    private void mapping(View view) {
        listViewOrder = view.findViewById(R.id.listviewMsell);

    }

    private void setEvent() {
        isSell = true;
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
                Button cancelOrder = dialog.findViewById(R.id.cancelOrder);

                cancelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            UserAPI.ApiGet(getContext(), Utils.BASE_URL + "order/delete-order/" + idOrder, new APICallBack() {
                                @Override
                                public void onSuccess(JSONObject response) throws JSONException {
                                    if (response.getLong("data") == 1) {
                                        dialog.dismiss();


                                        CustomToast.makeText(getContext(), "Hủy đơn đặt hàng thành công !", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();


                                    }else {
                                        dialog.dismiss();
                                        CustomToast.makeText(getContext(), "Hủy đơn đặt hàng không thành công !", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();

                                    }

                                }

                                @Override
                                public void onError(VolleyError error) {
    //                            CustomToast.makeText(myContext, "Hủy đơn đặt hàng không thành công !", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();
                                    dialog.dismiss();

                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        dialog.dismiss();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ApiConfirmDonHang(idOrder);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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

    private void ApiConfirmDonHang(String idOrder) throws JSONException {
        JSONObject postData = new JSONObject();
        PdfExporter.exportBillOrder(getContext(),hoaDonList ,"HoaDon"+idOrder+".pdf");
        CustomToast.makeText(getContext(), "Xác nhận đơn hàng thành công, vui lòng mở file để xem hóa đơn !", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();
        // gọi hàm in hóa đơn ở đây

        postData.put("id_seller",singletonUser.getIdUser() );
        postData.put("id_order", idOrder);
        postData.put("idPaymentMethod", 2);
        UserAPI.ApiPostandBody(getContext(), Utils.BASE_URL + "order/confirm", postData, new APICallBack() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
//                System.out.println("--------------------------");
//                PdfExporter.exportBillOrder(getContext(),hoaDonList ,"HoaDon"+idOrder+".pdf");
                CustomToast.makeText(getContext(), "Xác nhận đơn hàng thành công, vui lòng mở file để xem hóa đơn2 !", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();
                // gọi hàm in hóa đơn ở đây



            }

            @Override
            public void onError(VolleyError error) {
                CustomToast.makeText(getContext(), error.getMessage(), CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();

            }
        });
    }


}


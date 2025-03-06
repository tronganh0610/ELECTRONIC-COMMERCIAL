package com.example.appsportshop.fragment.Admin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.Login;
import com.example.appsportshop.activity.MainAdmin;
import com.example.appsportshop.activity.ManagerProductDetail;
import com.example.appsportshop.adapter.EmployeeManagerAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.ProductAPI;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.model.User;
import com.example.appsportshop.utils.CustomToast;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragManageEmloyee extends Fragment {

    JSONArray listEmployee = new JSONArray();
    ListView listViewEmployee;
    ArrayList<User> userArrayList ;

    private EmployeeManagerAdapter employeeManagerAdapter;

    Button btnAddEmployee;

    SearchView searchView;

    SingletonUser singletonUser = SingletonUser.getInstance();

    public static Boolean isDisplay = false;

    private void mapping(View view) {
        listViewEmployee = view.findViewById(R.id.listviewManagerEmployee);
        searchView = view.findViewById(R.id.seacrch_NV);
        searchView.setQueryHint("Nhập vào tên nhân viên để tìm kiếm");
        btnAddEmployee = view.findViewById(R.id.addEmployee);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_mn_emloyee, container, false);
        //lấy dữ liệu đc gửi từ trang Main
        mapping(view);
        //get All Emloyee
        try {
            UserAPI.getAllEmployeeOrCustomer(getContext(), Utils.BASE_URL + "user/getAllEmployee", new APICallBack() {
                @Override
                public void onSuccess(JSONObject response) throws JSONException {

                    listEmployee = response.getJSONArray("data");
                    JSONObject empObj = new JSONObject();
                    User userTmp;
                    userArrayList= new ArrayList<>();
                    for (int i = 0; i < listEmployee.length(); i++) {
                        empObj = (JSONObject) listEmployee.get(i);
                        userTmp = new User();
                        userTmp.setAvatarUrl(empObj.getString("avatarUrl"));
                        userTmp.setIdUser(empObj.getLong("id"));
                        userTmp.setFullName(empObj.getString("fullname"));
                        userTmp.setEmail(empObj.getString("email"));
                        userTmp.setPhone(empObj.getString("phone"));
                        userTmp.setRole(empObj.getString("role"));
                        userArrayList.add(userTmp);

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
        employeeManagerAdapter = new EmployeeManagerAdapter(getContext(), R.layout.row_manager_employee, userArrayList);
        listViewEmployee.setAdapter(employeeManagerAdapter);


//        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                positionCurrent = i;
//                product = ProductList.get(i);
//            }
//        });

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("abc123","test");
                popup();

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                productManagerAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                productManagerAdapter.getFilter().filter(s);
                ArrayList<User> list = new ArrayList<>();
                for (User user : userArrayList
                ) {

                    if (user.getFullName().toLowerCase().contains(s.toLowerCase()) || String.valueOf( user.getEmail()).toLowerCase().contains(s.toLowerCase()) ||
                    String.valueOf(user.getIdUser()).toLowerCase().contains(s.toLowerCase()) )  {
                        list.add(user);
                    }
                }
                employeeManagerAdapter = new EmployeeManagerAdapter(getContext(), R.layout.row_manager_employee, list);
                listViewEmployee.setAdapter(employeeManagerAdapter);


                return false;
            }
        });
    }

    private void popup() {
        Dialog dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.provice_acc);

        Button btnYes = dialog.findViewById(R.id.accept);
        Button btnNo = dialog.findViewById(R.id.cancel);
        EditText emailEdt = dialog.findViewById(R.id.tvEmailNv);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call api xóa

                try {
//                    com.example.ecomerceshoppe.ultils.dialog loadding = new dialog();
                    UserAPI.ProvideAccount(getContext(), Utils.BASE_URL + "user/createAccEmployee", emailEdt.getText().toString(), new APICallBack() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException {
                                dialog.dismiss();
                            Log.d("abc_1",response.toString());
                                CustomToast.makeText(getContext(), "Bạn đã cấp tài khoản cho nhân viên thành công !", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, true).show();

                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.d("abc",error.toString());
                            CustomToast.makeText(getContext(), "Email đã tồn tại trong hệ thống !", CustomToast.LENGTH_SHORT, CustomToast.ERROR, true).show();

                        }

                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                isDisplay = true;
               startActivity(new Intent(getContext(), MainAdmin.class));
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }



}

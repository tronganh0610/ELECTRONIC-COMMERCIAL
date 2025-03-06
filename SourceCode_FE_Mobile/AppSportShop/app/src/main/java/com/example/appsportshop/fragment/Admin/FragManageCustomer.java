package com.example.appsportshop.fragment.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.ManagerProductDetail;
import com.example.appsportshop.adapter.EmployeeManagerAdapter;
import com.example.appsportshop.api.APICallBack;
import com.example.appsportshop.api.UserAPI;
import com.example.appsportshop.model.User;
import com.example.appsportshop.utils.SingletonUser;
import com.example.appsportshop.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragManageCustomer extends Fragment {

    JSONArray listEmployee = new JSONArray();
    ListView listViewEmployee;
    ArrayList<User> userArrayList ;
    TextView checkManager;

    private EmployeeManagerAdapter employeeManagerAdapter;

    Button btnAddEmployee;

    SearchView searchView;

    SingletonUser singletonUser = SingletonUser.getInstance();

    public static Boolean isDisplay = false;

    private void mapping(View view) {
        checkManager = view.findViewById(R.id.checkManager);
        checkManager.setText("QUẢN LÍ TÀI KHOẢN KHÁCH HÀNG");
        listViewEmployee = view.findViewById(R.id.listviewManagerEmployee);
        searchView = view.findViewById(R.id.seacrch_NV);
        searchView.setQueryHint("Nhập vào tên Khách hàng để tìm kiếm");
        btnAddEmployee = view.findViewById(R.id.addEmployee);
        btnAddEmployee.setVisibility(View.GONE);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_mn_emloyee, container, false);
        //lấy dữ liệu đc gửi từ trang Main
        mapping(view);
        //get All Emloyee
        try {
            UserAPI.getAllEmployeeOrCustomer(getContext(), Utils.BASE_URL + "user/getAllCustomer", new APICallBack() {
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



        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManagerProductDetail.class);
                startActivity(intent);


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

                    if (user.getFullName().toLowerCase().contains(s.toLowerCase()) || String.valueOf( user.getEmail()).toLowerCase().contains(s.toLowerCase())  )  {
                        list.add(user);
                    }
                }
                employeeManagerAdapter = new EmployeeManagerAdapter(getContext(), R.layout.row_manager_employee, list);
                listViewEmployee.setAdapter(employeeManagerAdapter);


                return false;
            }
        });
    }



}

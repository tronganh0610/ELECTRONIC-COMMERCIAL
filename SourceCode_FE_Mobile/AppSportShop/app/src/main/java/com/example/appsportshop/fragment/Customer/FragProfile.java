package com.example.appsportshop.fragment.Customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appsportshop.R;
import com.example.appsportshop.activity.ChangePassW;
import com.example.appsportshop.activity.Login;
import com.example.appsportshop.activity.OrderItem;
import com.example.appsportshop.activity.Update_Profile;
import com.example.appsportshop.model.User;
import com.example.appsportshop.utils.ObjectWrapperForBinder;
import com.example.appsportshop.utils.SingletonUser;

public class FragProfile extends Fragment {

    public static Boolean isDisplay = false;


    ImageView avtUser;
    TextView nameUser;


    TextView fullName, ordered, emailProfile, phoneProfile, adressProfile, birthdayProfile, idProfile, changpass;

    LinearLayout logOut;


    LinearLayout btnLogout, btnUpdateProfile;


    SingletonUser userCurrent = SingletonUser.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_profile, container, false);


        mapping(view);

        if (userCurrent.getToken()== null) {
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        } else {
            loadInfoUser();

            setEvent();
        }


        return view;
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

    private void loadInfoUser() {

        if (userCurrent.getRole().equalsIgnoreCase("ADMIN"))
            ordered.setVisibility(View.GONE);
        if (userCurrent.getFullName() == null) {
            userCurrent.setFullName("");
        }

        if (userCurrent.getEmail() == null) {
            userCurrent.setEmail("");
        }
        if (userCurrent.getAdress() == null) {
            userCurrent.setAdress("");
        }

        if (userCurrent.getBirthday() == null) {
            birthdayProfile.setText("");

        } else {
            birthdayProfile.setText(userCurrent.getBirthday());

        }
        if (userCurrent.getPhone() == null) {
            userCurrent.setPhone("");
        }


        Glide.with(getContext()).load(userCurrent.getAvatarUrl()).into(avtUser);
        nameUser.setText(userCurrent.getFullName());
        idProfile.setText(String.valueOf(userCurrent.getIdUser()));
        emailProfile.setText(userCurrent.getEmail());
        fullName.setText(userCurrent.getFullName());
        phoneProfile.setText(userCurrent.getPhone());
        adressProfile.setText(userCurrent.getAdress());
//        birthdayProfile.setText(userCurrent.getBirthday());


    }


    private void mapping(View view) {
        changpass = view.findViewById(R.id.changpass);
        fullName = view.findViewById(R.id.fullname_Pf);
        emailProfile = view.findViewById(R.id.emailProfile);
        phoneProfile = view.findViewById(R.id.phoneProfile);
        adressProfile = view.findViewById(R.id.adressProfile);
        birthdayProfile = view.findViewById(R.id.birthdayProfile);
        idProfile = view.findViewById(R.id.id_user);
        avtUser = view.findViewById(R.id.avt_User);
        nameUser = view.findViewById(R.id.name_User);
        ordered = view.findViewById(R.id.ordered);

//        navi = view.findViewById(R.id.bottom_navigation_pro);
//        btnManagerProduct = view.findViewById(R.id.btnManagerProduct);
//        btnLogout = view.findViewById(R.id.logout);
        btnUpdateProfile = view.findViewById(R.id.updateInfo);
        logOut = view.findViewById(R.id.logout);
//        btnSetting = view.findViewById(R.id.setting);

    }

    private void setEvent() {
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("matkhau", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                SingletonUser singletonUser = SingletonUser.getInstance();
                singletonUser.clearValues();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });

        ordered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderItem.class);
                startActivity(intent);
            }
        });

        changpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePassW.class);
                startActivity(intent);
            }
        });


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), Update_Profile.class);
//                intent.putExtra("token",userCurrent);
//                startActivity(intent);


                Intent intent = new Intent(getContext(), Update_Profile.class);
                startActivity(intent);

            }
        });

    }


}
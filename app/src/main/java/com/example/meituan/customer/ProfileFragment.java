package com.example.meituan.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meituan.MainActivity;
import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.User;
import com.example.meituan.util.UiKit;

/**
 * 顾客端个人中心
 */
public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DataStore store = DataStore.getInstance();
        User user = store.getCurrentUser();
        if (user == null) user = com.example.meituan.data.MockData.customer();

        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvPhone = view.findViewById(R.id.tv_phone);
        tvName.setText(user.name);
        tvPhone.setText(maskPhone(user.phone));
        view.findViewById(R.id.avatar).setBackground(UiKit.circle(requireContext(), 0xFFFFFFFF));

        view.findViewById(R.id.entry_address).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AddressActivity.class)));
        view.findViewById(R.id.entry_coupon).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), CouponActivity.class)));
        view.findViewById(R.id.entry_review).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ReviewActivity.class)));

        view.findViewById(R.id.btn_logout).setOnClickListener(v -> {
            Toast.makeText(getContext(), "已退出登录", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) return phone;
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }
}

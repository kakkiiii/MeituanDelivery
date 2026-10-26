package com.example.meituan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meituan.customer.CustomerMainActivity;
import com.example.meituan.data.DataStore;
import com.example.meituan.data.MockData;
import com.example.meituan.merchant.MerchantMainActivity;
import com.example.meituan.model.User;

/**
 * 角色选择入口：选择顾客 / 商家身份进入对应端
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_customer).setOnClickListener(v ->
                enter(MockData.customer(), CustomerMainActivity.class));
        findViewById(R.id.btn_merchant).setOnClickListener(v ->
                enter(MockData.merchant(), MerchantMainActivity.class));
    }

    private void enter(User user, Class<?> cls) {
        DataStore.getInstance().setCurrentUser(user);
        startActivity(new Intent(this, cls));
    }
}

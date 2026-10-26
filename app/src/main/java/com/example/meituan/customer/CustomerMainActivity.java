package com.example.meituan.customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.meituan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * 顾客端主界面：底部导航（首页 / 订单 / 我的）
 */
public class CustomerMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        bottomNav = findViewById(R.id.bottom_nav);
        switchFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                switchFragment(new HomeFragment());
            } else if (id == R.id.nav_order) {
                switchFragment(new OrderFragment());
            } else if (id == R.id.nav_profile) {
                switchFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}

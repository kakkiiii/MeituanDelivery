package com.example.meituan.merchant;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Restaurant;

/**
 * 商家端营业设置
 */
public class BusinessSettingActivity extends AppCompatActivity {

    private DataStore store;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_setting);

        store = DataStore.getInstance();
        restaurant = store.getRestaurant(DataStore.MERCHANT_RESTAURANT_ID);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        SwitchCompat switchOpen = findViewById(R.id.switch_open);
        switchOpen.setChecked(restaurant.open);
        switchOpen.setOnCheckedChangeListener((buttonView, isChecked) -> restaurant.open = isChecked);

        TextView tvInfo = findViewById(R.id.tv_info);
        tvInfo.setText("营业时间：09:00 - 22:00\n"
                + "配送范围：3 公里内\n"
                + String.format("起送价：¥%.0f · 配送费 ¥%.2f", restaurant.minPrice, restaurant.deliveryFee));
    }
}

package com.example.meituan.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meituan.MainActivity;
import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Restaurant;

/**
 * 商家端主界面
 */
public class MerchantMainActivity extends AppCompatActivity {

    private DataStore store;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_main);

        store = DataStore.getInstance();
        restaurant = store.getRestaurant(DataStore.MERCHANT_RESTAURANT_ID);

        findViewById(R.id.entry_orders).setOnClickListener(v ->
                startActivity(new Intent(this, MerchantOrderActivity.class)));
        findViewById(R.id.entry_dishes).setOnClickListener(v ->
                startActivity(new Intent(this, DishManageActivity.class)));
        findViewById(R.id.entry_setting).setOnClickListener(v ->
                startActivity(new Intent(this, BusinessSettingActivity.class)));

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tvShop = findViewById(R.id.tv_shop_name);
        tvShop.setText(restaurant.name);
        TextView tvOpen = findViewById(R.id.tv_open_status);
        tvOpen.setText(restaurant.open ? "营业中" : "休息中");
        tvOpen.setBackgroundResource(restaurant.open
                ? R.drawable.bg_btn_teal : R.drawable.bg_btn_gray);
        tvOpen.setTextColor(restaurant.open
                ? getResources().getColor(android.R.color.white)
                : getResources().getColor(R.color.text_secondary));
        TextView tvToday = findViewById(R.id.tv_today);
        tvToday.setText("今日订单：" + store.getMerchantOrders().size() + " 单");
    }
}

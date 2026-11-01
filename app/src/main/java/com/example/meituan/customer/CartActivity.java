package com.example.meituan.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Dish;
import com.example.meituan.model.Order;
import com.example.meituan.model.Restaurant;

import java.util.List;
import java.util.Locale;

/**
 * 购物车
 */
public class CartActivity extends AppCompatActivity {

    private DataStore store;
    private CartAdapter adapter;
    private TextView tvTotal;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        store = DataStore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        emptyView = findViewById(R.id.empty_view);
        tvTotal = findViewById(R.id.tv_total);

        adapter = new CartAdapter(this, store.getCart(), new CartAdapter.Listener() {
            @Override
            public void onAdd(Dish d) {
                store.addToCart(d);
                refresh();
            }

            @Override
            public void onRemove(Dish d) {
                store.removeOneFromCart(d);
                refresh();
            }
        });
        RecyclerView recycler = findViewById(R.id.recycler_cart);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            store.clearCart();
            refresh();
        });

        findViewById(R.id.btn_checkout).setOnClickListener(v -> {
            // 下单前先剔除商家已下架的商品，避免买到不存在的菜
            int removed = store.purgeUnavailableFromCart();
            if (removed > 0) {
                refresh();
                Toast.makeText(this, "已移除 " + removed + " 个下架商品，请重新确认",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (store.cartCount() == 0) {
                Toast.makeText(this, "购物车为空", Toast.LENGTH_SHORT).show();
                return;
            }
            Restaurant restaurant = store.getRestaurant(store.cartRestaurantId());
            if (restaurant != null && store.cartTotal() < restaurant.minPrice) {
                Toast.makeText(this, String.format(Locale.CHINA,
                        "未满起送价 ¥%.0f，还差 ¥%.1f", restaurant.minPrice,
                        restaurant.minPrice - store.cartTotal()), Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, ConfirmOrderActivity.class));
        });

        refresh();
    }

    private void refresh() {
        List<Order.OrderItem> cart = store.getCart();
        adapter.setData(cart);
        tvTotal.setText(String.format(Locale.CHINA, "合计：¥%.2f", store.cartTotal()));
        emptyView.setVisibility(cart.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) refresh();
    }
}

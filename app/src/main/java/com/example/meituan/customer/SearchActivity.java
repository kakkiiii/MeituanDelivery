package com.example.meituan.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Dish;

import java.util.List;
import java.util.Locale;

/**
 * 搜索菜品
 */
public class SearchActivity extends AppCompatActivity {

    private DataStore store;
    private SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        store = DataStore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        EditText etSearch = findViewById(R.id.et_search);

        adapter = new SearchResultAdapter(this, store.searchDishes(""), this::addToCart);
        RecyclerView recycler = findViewById(R.id.recycler_result);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String kw = etSearch.getText().toString().trim();
            adapter.setData(store.searchDishes(kw));
            return true;
        });
    }

    private void addToCart(Dish d) {
        int cartRest = store.cartRestaurantId();
        if (cartRest != -1 && cartRest != d.restaurantId) {
            Toast.makeText(this, "购物车已有其他店铺商品，请先清空", Toast.LENGTH_SHORT).show();
            return;
        }
        store.addToCart(d);
        Toast.makeText(this, "已加入购物车", Toast.LENGTH_SHORT).show();
    }
}

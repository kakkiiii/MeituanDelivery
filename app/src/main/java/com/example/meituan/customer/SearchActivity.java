package com.example.meituan.customer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索菜品（支持菜品名 / 分类 / 店名）
 */
public class SearchActivity extends AppCompatActivity {

    private DataStore store;
    private SearchResultAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        store = DataStore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        EditText etSearch = findViewById(R.id.et_search);
        emptyView = findViewById(R.id.empty_view);

        adapter = new SearchResultAdapter(this, new ArrayList<>(), this::addToCart);
        RecyclerView recycler = findViewById(R.id.recycler_result);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        // 只在真正的"搜索"动作/回车时触发，避免误吞其他按键
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            boolean isEnter = event != null
                    && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            boolean isSearch = actionId == EditorInfo.IME_ACTION_SEARCH;
            if (!isEnter && !isSearch) return false;
            doSearch(etSearch.getText().toString().trim());
            return true;
        });

        findViewById(R.id.btn_search).setOnClickListener(v ->
                doSearch(etSearch.getText().toString().trim()));
    }

    private void doSearch(String kw) {
        List<Dish> result = store.searchDishes(kw);
        adapter.setData(result);
        emptyView.setVisibility(result.isEmpty() ? View.VISIBLE : View.GONE);
        if (result.isEmpty() && !kw.isEmpty()) {
            Toast.makeText(this, "没有找到相关菜品", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToCart(Dish d) {
        if (!d.available) {
            Toast.makeText(this, "该菜品已下架", Toast.LENGTH_SHORT).show();
            return;
        }
        int cartRest = store.cartRestaurantId();
        if (cartRest != -1 && cartRest != d.restaurantId) {
            Toast.makeText(this, "购物车已有其他店铺商品，请先清空", Toast.LENGTH_SHORT).show();
            return;
        }
        store.addToCart(d);
        Toast.makeText(this, "已加入购物车", Toast.LENGTH_SHORT).show();
    }
}

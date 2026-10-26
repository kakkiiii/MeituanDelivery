package com.example.meituan.merchant;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Dish;

import java.util.List;

/**
 * 商家端菜品管理：上架 / 下架
 */
public class DishManageActivity extends AppCompatActivity {

    private DataStore store;
    private DishManageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_manage);

        store = DataStore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new DishManageAdapter(this, dish -> {
            dish.available = !dish.available;
            Toast.makeText(this, dish.available ? "已上架" : "已下架", Toast.LENGTH_SHORT).show();
            refresh();
        });
        RecyclerView recycler = findViewById(R.id.recycler_dishes);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        refresh();
    }

    private void refresh() {
        List<Dish> dishes = store.getDishesByRestaurant(DataStore.MERCHANT_RESTAURANT_ID);
        adapter.setData(dishes);
    }
}

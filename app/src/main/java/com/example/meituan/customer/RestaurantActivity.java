package com.example.meituan.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.example.meituan.util.Images;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 店铺详情：左侧分类 + 右侧菜品 + 底部购物车栏
 */
public class RestaurantActivity extends AppCompatActivity {

    private DataStore store;
    private Restaurant restaurant;
    private List<Dish> allDishes;
    private DishAdapter dishAdapter;
    private TextView tvTotal;
    private TextView btnSettle;
    private TextView tvClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        store = DataStore.getInstance();
        int restaurantId = getIntent().getIntExtra("restaurant_id", 1);
        restaurant = store.getRestaurant(restaurantId);
        if (restaurant == null) {
            Toast.makeText(this, "店铺不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        allDishes = store.getDishesByRestaurant(restaurantId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(restaurant.name);
        toolbar.setNavigationOnClickListener(v -> finish());

        ImageView img = findViewById(R.id.img);
        Images.load(img, restaurant.image, restaurant.imageColor);
        TextView tvRating = findViewById(R.id.tv_rating);
        TextView tvMeta = findViewById(R.id.tv_meta);
        TextView tvNotice = findViewById(R.id.tv_notice);
        tvRating.setText(String.format(Locale.CHINA, "评分 %.1f", restaurant.rating));
        tvMeta.setText(String.format(Locale.CHINA, "月售%d · %d分钟 · %.1fkm",
                restaurant.monthlySales, restaurant.deliveryTime, restaurant.distance));
        tvNotice.setText(restaurant.notice);

        List<String> categories = new ArrayList<>(collectCategories());
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories, (index, cat) ->
                dishAdapter.setData(filterByCategory(cat)));
        RecyclerView recyclerCategory = findViewById(R.id.recycler_category);
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategory.setAdapter(categoryAdapter);

        dishAdapter = new DishAdapter(this, filterByCategory(categories.get(0)), new DishAdapter.Listener() {
            @Override
            public int getCount(Dish d) {
                for (Order.OrderItem it : store.getCart()) {
                    if (it.dish.id == d.id) return it.count;
                }
                return 0;
            }

            @Override
            public void onAdd(Dish d) {
                if (!d.available) {
                    Toast.makeText(RestaurantActivity.this, "该菜品已下架", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!restaurant.open) {
                    Toast.makeText(RestaurantActivity.this, "店铺休息中，暂不能下单", Toast.LENGTH_SHORT).show();
                    return;
                }
                int cartRest = store.cartRestaurantId();
                if (cartRest != -1 && cartRest != restaurant.id) {
                    Toast.makeText(RestaurantActivity.this,
                            "购物车已有其他店铺商品，请先清空", Toast.LENGTH_SHORT).show();
                    return;
                }
                store.addToCart(d);
                dishAdapter.notifyDataSetChanged();
                refreshBar();
            }

            @Override
            public void onRemove(Dish d) {
                store.removeOneFromCart(d);
                dishAdapter.notifyDataSetChanged();
                refreshBar();
            }
        });
        RecyclerView recyclerDish = findViewById(R.id.recycler_dish);
        recyclerDish.setLayoutManager(new LinearLayoutManager(this));
        recyclerDish.setAdapter(dishAdapter);

        tvTotal = findViewById(R.id.tv_total);
        btnSettle = findViewById(R.id.btn_settle);
        tvClosed = findViewById(R.id.tv_closed);
        if (!restaurant.open) tvClosed.setVisibility(View.VISIBLE);
        btnSettle.setOnClickListener(v -> {
            if (!restaurant.open) {
                Toast.makeText(this, "店铺休息中，暂不能下单", Toast.LENGTH_SHORT).show();
                return;
            }
            if (store.cartCount() == 0) {
                Toast.makeText(this, "请先添加商品", Toast.LENGTH_SHORT).show();
                return;
            }
            if (store.cartTotal() < restaurant.minPrice) {
                Toast.makeText(this, String.format(Locale.CHINA,
                        "未满起送价 ¥%.0f，还差 ¥%.1f", restaurant.minPrice,
                        restaurant.minPrice - store.cartTotal()), Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, CartActivity.class));
        });
        refreshBar();
    }

    private Set<String> collectCategories() {
        Set<String> set = new LinkedHashSet<>();
        for (Dish d : allDishes) {
            if (d.available) set.add(d.category);   // 只保留有在售菜品的分类
        }
        if (set.isEmpty()) set.add("全部");         // 极端情况：全部下架时兜底
        return set;
    }

    private List<Dish> filterByCategory(String cat) {
        List<Dish> result = new ArrayList<>();
        for (Dish d : allDishes) {
            if (d.available && d.category.equals(cat)) result.add(d);
        }
        return result;
    }

    private void refreshBar() {
        tvTotal.setText(String.format(Locale.CHINA, "¥%.2f", store.cartTotal()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dishAdapter != null) {
            dishAdapter.notifyDataSetChanged();
            refreshBar();
        }
    }
}

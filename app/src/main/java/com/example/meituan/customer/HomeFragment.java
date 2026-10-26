package com.example.meituan.customer;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Restaurant;
import com.example.meituan.util.UiKit;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 顾客端首页：搜索 + 分类 + 店铺列表
 */
public class HomeFragment extends Fragment {

    private DataStore store;
    private RecyclerView recycler;
    private RestaurantAdapter adapter;
    private LinearLayout categoryContainer;
    private String currentCategory = "全部";
    private List<Restaurant> allRestaurants;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        store = DataStore.getInstance();
        allRestaurants = store.getRestaurants();

        view.findViewById(R.id.search_bar).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), SearchActivity.class)));

        categoryContainer = view.findViewById(R.id.category_container);
        buildCategories();

        recycler = view.findViewById(R.id.recycler_restaurants);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestaurantAdapter(getContext(), filterByCategory(currentCategory), r -> {
            Intent i = new Intent(getActivity(), RestaurantActivity.class);
            i.putExtra("restaurant_id", r.id);
            startActivity(i);
        });
        recycler.setAdapter(adapter);
    }

    private void buildCategories() {
        categoryContainer.removeAllViews();
        Set<String> cats = new LinkedHashSet<>();
        cats.add("全部");
        for (Restaurant r : allRestaurants) cats.add(r.category);
        for (String cat : cats) {
            TextView tv = new TextView(getContext());
            tv.setText(cat);
            tv.setTextSize(13);
            tv.setPadding(dp(16), dp(7), dp(16), dp(7));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, dp(8), 0);
            tv.setLayoutParams(lp);
            tv.setOnClickListener(v -> {
                currentCategory = cat;
                refreshChipStyles();
                adapter.setData(filterByCategory(cat));
            });
            categoryContainer.addView(tv);
        }
        refreshChipStyles();
    }

    private void refreshChipStyles() {
        for (int i = 0; i < categoryContainer.getChildCount(); i++) {
            TextView tv = (TextView) categoryContainer.getChildAt(i);
            boolean selected = tv.getText().toString().equals(currentCategory);
            tv.setBackground(UiKit.roundRect(requireContext(),
                    selected ? 0xFF33230A : 0xFFFFFFFF, 20));
            tv.setTextColor(selected ? 0xFFFFD100 : 0xFF666666);
            tv.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
        }
    }

    private List<Restaurant> filterByCategory(String cat) {
        if ("全部".equals(cat)) return new ArrayList<>(allRestaurants);
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant r : allRestaurants) {
            if (r.category.equals(cat)) result.add(r);
        }
        return result;
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}

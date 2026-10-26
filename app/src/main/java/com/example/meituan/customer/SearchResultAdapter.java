package com.example.meituan.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Dish;
import com.example.meituan.util.UiKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 搜索结果适配器
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.VH> {

    public interface Listener {
        void onAdd(Dish d);
    }

    private final Context context;
    private final DataStore store;
    private final List<Dish> data = new ArrayList<>();
    private final Listener listener;

    public SearchResultAdapter(Context context, List<Dish> list, Listener listener) {
        this.context = context;
        this.store = DataStore.getInstance();
        this.listener = listener;
        data.addAll(list);
    }

    public void setData(List<Dish> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Dish d = data.get(position);
        h.img.setBackground(UiKit.roundRect(context, d.imageColor, 8));
        h.tvName.setText(d.name);
        h.tvRestaurant.setText(store.getRestaurant(d.restaurantId).name);
        h.tvPrice.setText(String.format(Locale.CHINA, "¥%.2f", d.price));
        h.btnAdd.setOnClickListener(v -> listener.onAdd(d));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View img;
        TextView tvName, tvRestaurant, tvPrice, btnAdd;

        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRestaurant = itemView.findViewById(R.id.tv_restaurant);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnAdd = itemView.findViewById(R.id.btn_add);
        }
    }
}

package com.example.meituan.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.model.Restaurant;
import com.example.meituan.util.Images;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 首页店铺列表适配器
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.VH> {

    public interface OnItemClick {
        void onClick(Restaurant r);
    }

    private final Context context;
    private final List<Restaurant> data = new ArrayList<>();
    private final OnItemClick listener;

    public RestaurantAdapter(Context context, List<Restaurant> list, OnItemClick listener) {
        this.context = context;
        this.listener = listener;
        data.addAll(list);
    }

    public void setData(List<Restaurant> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Restaurant r = data.get(position);
        Images.load(h.img, r.image, r.imageColor);
        h.tvName.setText(r.name);
        h.tvRating.setText(String.format(Locale.CHINA, "%.1f分", r.rating));
        h.tvSales.setText("月售" + r.monthlySales);
        h.tvTime.setText(r.deliveryTime + "分钟");
        h.tvFee.setText(String.format(Locale.CHINA, "起送¥%.0f · 配送¥%.2f", r.minPrice, r.deliveryFee));
        h.tvDistance.setText(String.format(Locale.CHINA, "%.1fkm", r.distance));
        h.tvNotice.setText(r.category);
        // 每次都要赋值：否则复用的 ViewHolder 会残留"（休息中）"后缀
        h.tvName.setText(r.open ? r.name : r.name + "（休息中）");
        h.tvName.setTextColor(r.open
                ? ContextCompat.getColor(context, R.color.text_primary)
                : ContextCompat.getColor(context, R.color.text_hint));
        h.itemView.setOnClickListener(v -> listener.onClick(r));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvRating, tvSales, tvTime, tvFee, tvDistance, tvNotice;

        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvSales = itemView.findViewById(R.id.tv_sales);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvFee = itemView.findViewById(R.id.tv_fee);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvNotice = itemView.findViewById(R.id.tv_notice);
        }
    }
}

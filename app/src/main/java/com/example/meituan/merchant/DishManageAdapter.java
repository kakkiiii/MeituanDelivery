package com.example.meituan.merchant;

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
import com.example.meituan.model.Dish;
import com.example.meituan.util.Images;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 菜品管理适配器
 */
public class DishManageAdapter extends RecyclerView.Adapter<DishManageAdapter.VH> {

    public interface Listener {
        void onToggle(Dish d);
    }

    private final Context context;
    private final List<Dish> data = new ArrayList<>();
    private final Listener listener;

    public DishManageAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<Dish> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_manage_dish, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Dish d = data.get(position);
        Images.load(h.img, d.image, d.imageColor);
        h.tvName.setText(d.name);
        h.tvPrice.setText(String.format(Locale.CHINA, "¥%.2f · 月售%d", d.price, d.monthlySales));

        if (d.available) {
            h.btnToggle.setText("下架");
            h.btnToggle.setBackgroundResource(R.drawable.bg_btn_gray);
            h.btnToggle.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        } else {
            h.btnToggle.setText("上架");
            h.btnToggle.setBackgroundResource(R.drawable.bg_btn_teal);
            h.btnToggle.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        h.btnToggle.setOnClickListener(v -> listener.onToggle(d));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvPrice, btnToggle;

        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnToggle = itemView.findViewById(R.id.btn_toggle);
        }
    }
}

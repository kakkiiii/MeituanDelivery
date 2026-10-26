package com.example.meituan.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.model.Dish;
import com.example.meituan.util.UiKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 店铺详情 - 菜品列表适配器（含加减购物车交互）
 */
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.VH> {

    public interface Listener {
        int getCount(Dish d);
        void onAdd(Dish d);
        void onRemove(Dish d);
    }

    private final Context context;
    private final List<Dish> data = new ArrayList<>();
    private final Listener listener;

    public DishAdapter(Context context, List<Dish> list, Listener listener) {
        this.context = context;
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
        View v = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Dish d = data.get(position);
        h.tvName.setText(d.name);
        h.tvDesc.setText(d.description);
        h.tvSales.setText("月售 " + d.monthlySales);
        h.tvPrice.setText(String.format(Locale.CHINA, "¥%.2f", d.price));
        h.img.setBackground(UiKit.roundRect(context, d.imageColor, 8));

        int count = listener.getCount(d);
        if (count > 0) {
            h.btnMinus.setVisibility(View.VISIBLE);
            h.tvCount.setVisibility(View.VISIBLE);
            h.tvCount.setText(String.valueOf(count));
        } else {
            h.btnMinus.setVisibility(View.GONE);
            h.tvCount.setVisibility(View.GONE);
        }

        h.btnPlus.setOnClickListener(v -> listener.onAdd(d));
        h.btnMinus.setOnClickListener(v -> listener.onRemove(d));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvSales, tvPrice, btnMinus, btnPlus, tvCount;
        View img;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvSales = itemView.findViewById(R.id.tv_sales);
            tvPrice = itemView.findViewById(R.id.tv_price);
            img = itemView.findViewById(R.id.img);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            tvCount = itemView.findViewById(R.id.tv_count);
        }
    }
}

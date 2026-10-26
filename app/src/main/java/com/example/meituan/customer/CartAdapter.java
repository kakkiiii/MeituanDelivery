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
import com.example.meituan.model.Order;
import com.example.meituan.util.UiKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 购物车适配器
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    public interface Listener {
        void onAdd(Dish d);
        void onRemove(Dish d);
    }

    private final Context context;
    private final List<Order.OrderItem> data = new ArrayList<>();
    private final Listener listener;

    public CartAdapter(Context context, List<Order.OrderItem> list, Listener listener) {
        this.context = context;
        this.listener = listener;
        data.addAll(list);
    }

    public void setData(List<Order.OrderItem> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Order.OrderItem it = data.get(position);
        Dish d = it.dish;
        h.img.setBackground(UiKit.roundRect(context, d.imageColor, 8));
        h.tvName.setText(d.name);
        h.tvPrice.setText(String.format(Locale.CHINA, "¥%.2f", d.price));
        h.tvCount.setText(String.valueOf(it.count));
        h.btnPlus.setOnClickListener(v -> listener.onAdd(d));
        h.btnMinus.setOnClickListener(v -> listener.onRemove(d));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View img;
        TextView tvName, tvPrice, tvCount, btnPlus, btnMinus;

        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCount = itemView.findViewById(R.id.tv_count);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }
}

package com.example.meituan.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 确认订单页 - 菜品明细适配器（只读）
 */
public class ConfirmDishAdapter extends RecyclerView.Adapter<ConfirmDishAdapter.VH> {

    private final Context context;
    private final List<Order.OrderItem> data = new ArrayList<>();

    public ConfirmDishAdapter(Context context, List<Order.OrderItem> list) {
        this.context = context;
        data.addAll(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_confirm_dish, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Order.OrderItem it = data.get(position);
        h.tvName.setText(it.dish.name);
        h.tvCount.setText("x" + it.count);
        h.tvSubtotal.setText(String.format(Locale.CHINA, "¥%.2f", it.subTotal()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvCount, tvSubtotal;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCount = itemView.findViewById(R.id.tv_count);
            tvSubtotal = itemView.findViewById(R.id.tv_subtotal);
        }
    }
}

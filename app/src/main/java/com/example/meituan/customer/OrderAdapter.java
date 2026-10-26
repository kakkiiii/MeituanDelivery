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
import com.example.meituan.model.Order;
import com.example.meituan.util.Images;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 订单列表适配器
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.VH> {

    public interface Listener {
        void onDetail(Order order);
        void onPrimary(Order order);
    }

    private final Context context;
    private final List<Order> data = new ArrayList<>();
    private final Listener listener;

    public OrderAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<Order> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Order o = data.get(position);
        h.tvRestaurant.setText(o.restaurant.name);
        h.tvStatus.setText(Order.statusText(o.status));
        h.tvStatus.setTextColor(statusColor(o.status));
        h.tvItems.setText(o.itemsSummary());
        h.tvTotal.setText(String.format(Locale.CHINA, "¥%.2f", o.payable));
        h.tvTime.setText(o.createTime);
        Images.load(h.img, o.restaurant.image, o.restaurant.imageColor, 10);

        if (o.status == Order.STATUS_DELIVERED) {
            h.btnPrimary.setVisibility(View.VISIBLE);
            h.btnPrimary.setText("去评价");
        } else if (o.status == Order.STATUS_PENDING) {
            h.btnPrimary.setVisibility(View.VISIBLE);
            h.btnPrimary.setText("取消订单");
        } else if (o.status == Order.STATUS_READY) {
            h.btnPrimary.setVisibility(View.VISIBLE);
            h.btnPrimary.setText("模拟送达");
        } else {
            h.btnPrimary.setVisibility(View.GONE);
        }

        h.btnPrimary.setOnClickListener(v -> listener.onPrimary(o));
        h.btnDetail.setOnClickListener(v -> listener.onDetail(o));
    }

    private int statusColor(int status) {
        switch (status) {
            case Order.STATUS_CANCELED:
                return ContextCompat.getColor(context, R.color.text_hint);
            case Order.STATUS_DELIVERED:
            case Order.STATUS_COMPLETED:
                return ContextCompat.getColor(context, R.color.green);
            default:
                return ContextCompat.getColor(context, R.color.accent);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvRestaurant, tvStatus, tvItems, tvTotal, tvTime, btnPrimary, btnDetail;

        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tvRestaurant = itemView.findViewById(R.id.tv_restaurant);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvItems = itemView.findViewById(R.id.tv_items);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnPrimary = itemView.findViewById(R.id.btn_primary);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}

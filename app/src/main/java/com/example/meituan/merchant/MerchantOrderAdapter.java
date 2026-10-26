package com.example.meituan.merchant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 商家订单列表适配器
 */
public class MerchantOrderAdapter extends RecyclerView.Adapter<MerchantOrderAdapter.VH> {

    public interface Listener {
        void onPrimary(Order o);
        void onSecondary(Order o);
    }

    private final Context context;
    private final List<Order> data = new ArrayList<>();
    private final Listener listener;

    public MerchantOrderAdapter(Context context, Listener listener) {
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
        View v = LayoutInflater.from(context).inflate(R.layout.item_merchant_order, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Order o = data.get(position);
        h.tvOrderNo.setText("订单号 " + o.orderNo);
        h.tvStatus.setText(Order.statusText(o.status));
        h.tvStatus.setTextColor(statusColor(o.status));
        h.tvItems.setText(o.itemsSummary());
        h.tvAddress.setText(o.address == null ? "无地址" : o.address.full());
        h.tvAmount.setText(String.format(Locale.CHINA, "¥%.2f", o.payable));

        if (o.status == Order.STATUS_PENDING) {
            h.btnPrimary.setVisibility(View.VISIBLE);
            h.btnPrimary.setText("接单");
            h.btnSecondary.setVisibility(View.VISIBLE);
            h.btnSecondary.setText("拒绝");
        } else if (o.status == Order.STATUS_MERCHANT_ACCEPTED) {
            h.btnPrimary.setVisibility(View.VISIBLE);
            h.btnPrimary.setText("出餐");
            h.btnSecondary.setVisibility(View.GONE);
        } else {
            h.btnPrimary.setVisibility(View.GONE);
            h.btnSecondary.setVisibility(View.GONE);
        }

        h.btnPrimary.setOnClickListener(v -> listener.onPrimary(o));
        h.btnSecondary.setOnClickListener(v -> listener.onSecondary(o));
    }

    private int statusColor(int status) {
        switch (status) {
            case Order.STATUS_CANCELED:
                return ContextCompat.getColor(context, R.color.text_hint);
            case Order.STATUS_READY:
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
        TextView tvOrderNo, tvStatus, tvItems, tvAddress, tvAmount, btnPrimary, btnSecondary;

        VH(@NonNull View itemView) {
            super(itemView);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvItems = itemView.findViewById(R.id.tv_items);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            btnPrimary = itemView.findViewById(R.id.btn_primary);
            btnSecondary = itemView.findViewById(R.id.btn_secondary);
        }
    }
}

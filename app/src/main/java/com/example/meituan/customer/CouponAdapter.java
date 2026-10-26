package com.example.meituan.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.model.Coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 优惠券列表适配器
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.VH> {

    private final Context context;
    private final List<Coupon> data = new ArrayList<>();

    public CouponAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Coupon> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Coupon c = data.get(position);
        h.tvAmount.setText(String.format(Locale.CHINA, "¥%.0f", c.amount));
        h.tvTitle.setText(c.title);
        h.tvThreshold.setText(c.threshold > 0
                ? String.format(Locale.CHINA, "满 %.0f 元可用", c.threshold)
                : "无门槛");
        if (c.used) {
            h.tvStatus.setText("已使用");
            h.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.text_hint));
        } else {
            h.tvStatus.setText("去使用");
            h.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.accent));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvAmount, tvTitle, tvThreshold, tvStatus;

        VH(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvThreshold = itemView.findViewById(R.id.tv_threshold);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}

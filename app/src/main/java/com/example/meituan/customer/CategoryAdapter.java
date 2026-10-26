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

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺详情 - 左侧分类适配器
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    public interface OnSelect {
        void onSelect(int index, String category);
    }

    private final Context context;
    private final List<String> data = new ArrayList<>();
    private final OnSelect listener;
    private int selected = 0;

    public CategoryAdapter(Context context, List<String> list, OnSelect listener) {
        this.context = context;
        this.listener = listener;
        data.addAll(list);
    }

    public void setSelected(int index) {
        selected = index;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        String cat = data.get(position);
        h.tv.setText(cat);
        boolean sel = position == selected;
        h.tv.setTextColor(sel
                ? ContextCompat.getColor(context, R.color.text_primary)
                : ContextCompat.getColor(context, R.color.text_secondary));
        h.tv.setTypeface(null, sel ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        h.itemView.setBackgroundColor(sel
                ? ContextCompat.getColor(context, R.color.white)
                : ContextCompat.getColor(context, R.color.gray_bg));
        h.indicator.setVisibility(sel ? View.VISIBLE : View.INVISIBLE);
        h.itemView.setOnClickListener(v -> {
            selected = position;
            notifyDataSetChanged();
            listener.onSelect(position, cat);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        View indicator;

        VH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_category);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}

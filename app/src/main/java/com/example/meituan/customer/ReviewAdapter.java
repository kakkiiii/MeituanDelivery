package com.example.meituan.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * 评价列表适配器
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.VH> {

    private final Context context;
    private final List<Review> data = new ArrayList<>();

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Review> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Review r = data.get(position);
        h.tvRestaurant.setText(r.restaurantName);
        h.tvStars.setText(stars(r.rating));
        h.tvContent.setText(r.content);
        h.tvTime.setText(r.time);
    }

    private String stars(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(i < rating ? "★" : "☆");
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRestaurant, tvStars, tvContent, tvTime;

        VH(@NonNull View itemView) {
            super(itemView);
            tvRestaurant = itemView.findViewById(R.id.tv_restaurant);
            tvStars = itemView.findViewById(R.id.tv_stars);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}

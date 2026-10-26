package com.example.meituan.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.model.ChatMessage;
import com.example.meituan.util.Images;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 聊天消息适配器：用户气泡 / 机器人文本 / 推荐卡片
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {

    public interface OnGoShop {
        void onGo(ChatMessage m);
    }

    private final List<ChatMessage> data = new ArrayList<>();
    private final OnGoShop listener;

    public ChatAdapter(OnGoShop listener) {
        this.listener = listener;
    }

    public void add(ChatMessage m) {
        data.add(m);
        notifyItemInserted(data.size() - 1);
    }

    public ChatMessage last() {
        return data.isEmpty() ? null : data.get(data.size() - 1);
    }

    public void replaceLast(ChatMessage m) {
        if (data.isEmpty()) return;
        data.set(data.size() - 1, m);
        notifyItemChanged(data.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage m = data.get(position);
        if (m.fromUser) return 0;
        return m.recommend ? 2 : 1;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == 0 ? R.layout.item_chat_user
                : viewType == 2 ? R.layout.item_chat_recommend
                : R.layout.item_chat_bot;
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        ChatMessage m = data.get(position);
        if (m.fromUser) {
            h.tvText.setText(m.text);
            return;
        }
        if (m.recommend) {
            h.tvBadge.setText(m.fromAi ? "DeepSeek AI 推荐" : "本地智能推荐");
            h.tvReason.setText(m.reason);
            h.tvDish.setText(m.dishName);
            h.tvShop.setText(m.restaurantName);
            h.tvPrice.setText("¥" + m.dishPrice);
            Images.load(h.img, m.dishImage, 0xFFFFF3C4);
            h.btnGo.setOnClickListener(v -> listener.onGo(m));
        } else {
            h.tvText.setText(m.text);
            if (m.thinking) h.tvText.setTextColor(0xFF999999);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvText, tvBadge, tvReason, tvDish, tvShop, tvPrice, btnGo;
        ImageView img;

        VH(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
            tvBadge = itemView.findViewById(R.id.tv_badge);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvDish = itemView.findViewById(R.id.tv_dish);
            tvShop = itemView.findViewById(R.id.tv_shop);
            tvPrice = itemView.findViewById(R.id.tv_price);
            btnGo = itemView.findViewById(R.id.btn_go);
            img = itemView.findViewById(R.id.img);
        }
    }
}

package com.example.meituan.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.model.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址列表适配器
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.VH> {

    public interface Listener {
        void onSetDefault(Address a);
        void onDelete(Address a);
    }

    private final Context context;
    private final List<Address> data = new ArrayList<>();
    private final Listener listener;

    public AddressAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<Address> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Address a = data.get(position);
        h.tvName.setText(a.name);
        h.tvPhone.setText(a.phone);
        h.tvDetail.setText(a.detail);
        h.tvDefaultTag.setVisibility(a.isDefault ? View.VISIBLE : View.GONE);
        h.btnDefault.setOnClickListener(v -> listener.onSetDefault(a));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(a));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvDetail, tvDefaultTag, btnDefault, btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvDetail = itemView.findViewById(R.id.tv_detail);
            tvDefaultTag = itemView.findViewById(R.id.tv_default_tag);
            btnDefault = itemView.findViewById(R.id.btn_default);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}

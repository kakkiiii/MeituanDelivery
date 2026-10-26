package com.example.meituan.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Order;

import java.util.List;

/**
 * 顾客端订单列表
 */
public class OrderFragment extends Fragment {

    private DataStore store;
    private RecyclerView recycler;
    private TextView emptyView;
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        store = DataStore.getInstance();
        recycler = view.findViewById(R.id.recycler_orders);
        emptyView = view.findViewById(R.id.empty_view);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(getContext(), new OrderAdapter.Listener() {
            @Override
            public void onDetail(Order order) {
                startActivity(new Intent(getActivity(), OrderDetailActivity.class)
                        .putExtra("order_id", order.id));
            }

            @Override
            public void onPrimary(Order order) {
                if (order.status == Order.STATUS_DELIVERED) {
                    startActivity(new Intent(getActivity(), ReviewActivity.class)
                            .putExtra("order_id", order.id));
                } else if (order.status == Order.STATUS_PENDING) {
                    if (store.cancelOrder(order)) {
                        Toast.makeText(getContext(), "订单已取消", Toast.LENGTH_SHORT).show();
                        refresh();
                    }
                } else if (order.status == Order.STATUS_READY) {
                    if (store.deliverOrder(order)) {
                        Toast.makeText(getContext(), "已送达，等待评价", Toast.LENGTH_SHORT).show();
                        refresh();
                    }
                }
            }
        });
        recycler.setAdapter(adapter);
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) refresh();
    }

    private void refresh() {
        List<Order> orders = store.getCustomerOrders();
        adapter.setData(orders);
        emptyView.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
    }
}

package com.example.meituan.merchant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Order;

import java.util.List;

/**
 * 商家端订单管理：接单 / 拒绝 / 出餐
 */
public class MerchantOrderActivity extends AppCompatActivity {

    private DataStore store;
    private MerchantOrderAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order);

        store = DataStore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        emptyView = findViewById(R.id.empty_view);
        adapter = new MerchantOrderAdapter(this, new MerchantOrderAdapter.Listener() {
            @Override
            public void onPrimary(Order o) {
                if (o.status == Order.STATUS_PENDING) {
                    store.merchantAccept(o);
                    Toast.makeText(MerchantOrderActivity.this, "已接单，请尽快备餐", Toast.LENGTH_SHORT).show();
                } else if (o.status == Order.STATUS_MERCHANT_ACCEPTED) {
                    store.merchantReady(o);
                    Toast.makeText(MerchantOrderActivity.this, "已出餐，配送中", Toast.LENGTH_SHORT).show();
                }
                refresh();
            }

            @Override
            public void onSecondary(Order o) {
                boolean ok = store.merchantReject(o);
                Toast.makeText(MerchantOrderActivity.this,
                        ok ? "已拒绝该订单" : "当前状态无法拒绝（已接单或已结束）",
                        Toast.LENGTH_SHORT).show();
                refresh();
            }
        });
        RecyclerView recycler = findViewById(R.id.recycler_orders);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) refresh();
    }

    private void refresh() {
        List<Order> orders = store.getMerchantOrders();
        adapter.setData(orders);
        emptyView.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
    }
}

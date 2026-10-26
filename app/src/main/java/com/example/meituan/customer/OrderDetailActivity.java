package com.example.meituan.customer;

import android.content.Intent;
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
import com.example.meituan.util.UiKit;

import java.util.Locale;

/**
 * 订单详情
 */
public class OrderDetailActivity extends AppCompatActivity {

    private DataStore store;
    private Order order;
    private TextView btnAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        store = DataStore.getInstance();
        int orderId = getIntent().getIntExtra("order_id", -1);
        order = store.getOrder(orderId);
        if (order == null) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        View img = findViewById(R.id.img);
        img.setBackground(UiKit.roundRect(this, order.restaurant.imageColor, 8));
        TextView tvRestaurant = findViewById(R.id.tv_restaurant);
        tvRestaurant.setText(order.restaurant.name);

        RecyclerView recycler = findViewById(R.id.recycler_items);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new ConfirmDishAdapter(this, order.items));

        TextView tvGoodsTotal = findViewById(R.id.tv_goods_total);
        tvGoodsTotal.setText(String.format(Locale.CHINA, "¥%.2f", order.totalPrice));
        TextView tvDelivery = findViewById(R.id.tv_delivery);
        tvDelivery.setText(String.format(Locale.CHINA, "¥%.2f", order.deliveryFee));
        TextView tvCoupon = findViewById(R.id.tv_coupon);
        if (order.couponAmount > 0) {
            tvCoupon.setText(String.format(Locale.CHINA, "-¥%.2f", order.couponAmount));
        } else {
            findViewById(R.id.coupon_line).setVisibility(View.GONE);
        }
        TextView tvPayable = findViewById(R.id.tv_payable);
        tvPayable.setText(String.format(Locale.CHINA, "¥%.2f", order.payable));

        StringBuilder info = new StringBuilder();
        info.append("订单号：").append(order.orderNo).append("\n");
        info.append("下单时间：").append(order.createTime).append("\n");
        info.append("备注：").append(order.remark == null || order.remark.isEmpty() ? "无" : order.remark).append("\n");
        info.append("收货地址：").append(order.address == null ? "无" : order.address.full()).append("\n");
        info.append("配送方式：平台配送");
        if (!order.deliverTime.isEmpty()) {
            info.append("\n送达时间：").append(order.deliverTime);
        }
        TextView tvInfo = findViewById(R.id.tv_info);
        tvInfo.setText(info.toString());

        btnAction = findViewById(R.id.btn_action);
        refreshStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStatus();
    }

    /** 刷新状态展示与底部操作按钮（评价完成返回时同步更新） */
    private void refreshStatus() {
        TextView tvStatus = findViewById(R.id.tv_status);
        tvStatus.setText(Order.statusText(order.status));

        if (order.status == Order.STATUS_PENDING) {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("取消订单");
            btnAction.setOnClickListener(v -> {
                if (store.cancelOrder(order)) {
                    Toast.makeText(this, "订单已取消", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else if (order.status == Order.STATUS_READY) {
            // 配送中（模拟）：点击模拟送达，进入待评价
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("模拟送达");
            btnAction.setOnClickListener(v -> {
                if (store.deliverOrder(order)) {
                    Toast.makeText(this, "已送达", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else if (order.status == Order.STATUS_DELIVERED) {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("去评价");
            btnAction.setOnClickListener(v ->
                    startActivity(new Intent(this, ReviewActivity.class)
                            .putExtra("order_id", order.id)));
        } else {
            btnAction.setVisibility(View.GONE);
        }
    }
}

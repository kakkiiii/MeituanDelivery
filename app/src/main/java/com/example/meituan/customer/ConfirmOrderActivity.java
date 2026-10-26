package com.example.meituan.customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Address;
import com.example.meituan.model.Coupon;
import com.example.meituan.model.Order;
import com.example.meituan.model.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 确认订单：地址 / 优惠券 / 备注 / 提交（模拟支付）
 */
public class ConfirmOrderActivity extends AppCompatActivity {

    private DataStore store;
    private Address selectedAddress;
    private Coupon selectedCoupon;
    private Restaurant restaurant;

    private TextView tvAddress, tvRestaurant, tvCoupon, tvDeliveryFee, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        store = DataStore.getInstance();
        int cartRestId = store.cartRestaurantId();
        restaurant = store.getRestaurant(cartRestId);
        if (cartRestId == -1 || restaurant == null) {
            Toast.makeText(this, "购物车为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        selectedAddress = store.getDefaultAddress();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvAddress = findViewById(R.id.tv_address);
        tvRestaurant = findViewById(R.id.tv_restaurant);
        tvCoupon = findViewById(R.id.tv_coupon);
        tvDeliveryFee = findViewById(R.id.tv_delivery_fee);
        tvTotal = findViewById(R.id.tv_total);

        tvRestaurant.setText(restaurant.name);

        RecyclerView recycler = findViewById(R.id.recycler_items);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new ConfirmDishAdapter(this, store.getCart()));

        findViewById(R.id.address_card).setOnClickListener(v -> showAddressDialog());
        findViewById(R.id.coupon_row).setOnClickListener(v -> showCouponDialog());

        EditText etRemark = findViewById(R.id.et_remark);

        findViewById(R.id.btn_submit).setOnClickListener(v -> {
            if (selectedAddress == null) {
                Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
                return;
            }
            submitOrder(etRemark.getText().toString().trim());
        });

        updateUi();
    }

    private void submitOrder(String remark) {
        Order order = new Order();
        order.restaurant = restaurant;
        order.items.addAll(store.getCart());
        order.totalPrice = store.cartTotal();
        order.deliveryFee = restaurant.deliveryFee;
        order.couponAmount = selectedCoupon == null ? 0 : selectedCoupon.amount;
        double couponCut = Math.min(order.couponAmount, order.totalPrice + order.deliveryFee);
        order.payable = Math.max(0, order.totalPrice + order.deliveryFee - couponCut);
        order.address = selectedAddress;
        order.remark = remark.isEmpty() ? "" : remark;

        store.placeOrder(order);
        if (selectedCoupon != null) selectedCoupon.used = true;

        Toast.makeText(this, "支付成功，下单完成！", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, OrderDetailActivity.class)
                .putExtra("order_id", order.id));
        finish();
    }

    private void showAddressDialog() {
        List<Address> addresses = store.getAddresses();
        String[] items = new String[addresses.size()];
        for (int i = 0; i < addresses.size(); i++) {
            items[i] = addresses.get(i).full();
        }
        new AlertDialog.Builder(this)
                .setTitle("选择收货地址")
                .setItems(items, (dialog, which) -> {
                    selectedAddress = addresses.get(which);
                    updateUi();
                })
                .show();
    }

    private void showCouponDialog() {
        List<Coupon> available = new ArrayList<>();
        double total = store.cartTotal();
        for (Coupon c : store.getCoupons()) {
            if (!c.used && c.threshold <= total) available.add(c);
        }
        if (available.isEmpty()) {
            Toast.makeText(this, "暂无可用优惠券", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] items = new String[available.size() + 1];
        items[0] = "不使用优惠券";
        for (int i = 0; i < available.size(); i++) {
            Coupon c = available.get(i);
            items[i + 1] = String.format(Locale.CHINA, "%s（-¥%.0f）", c.title, c.amount);
        }
        new AlertDialog.Builder(this)
                .setTitle("选择优惠券")
                .setItems(items, (dialog, which) -> {
                    selectedCoupon = which == 0 ? null : available.get(which - 1);
                    updateUi();
                })
                .show();
    }

    private void updateUi() {
        tvAddress.setText(selectedAddress == null ? "请选择收货地址" : selectedAddress.full());
        tvDeliveryFee.setText(String.format(Locale.CHINA, "¥%.2f", restaurant.deliveryFee));
        tvCoupon.setText(selectedCoupon == null
                ? "选择优惠券 ›"
                : String.format(Locale.CHINA, "-¥%.2f", selectedCoupon.amount));

        double total = store.cartTotal();
        double cut = selectedCoupon == null ? 0
                : Math.min(selectedCoupon.amount, total + restaurant.deliveryFee);
        double payable = Math.max(0, total + restaurant.deliveryFee - cut);
        tvTotal.setText(String.format(Locale.CHINA, "¥%.2f", payable));
    }
}

package com.example.meituan.customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Address;

/**
 * 地址管理
 */
public class AddressActivity extends AppCompatActivity {

    private DataStore store;
    private AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        store = DataStore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new AddressAdapter(this, new AddressAdapter.Listener() {
            @Override
            public void onSetDefault(Address a) {
                store.setDefaultAddress(a);
                refresh();
            }

            @Override
            public void onDelete(Address a) {
                store.removeAddress(a);
                refresh();
                Toast.makeText(AddressActivity.this, "已删除", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView recycler = findViewById(R.id.recycler_address);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        findViewById(R.id.btn_add).setOnClickListener(v -> showAddDialog());
        refresh();
    }

    private void showAddDialog() {
        EditText etName = new EditText(this);
        etName.setHint("联系人");
        etName.setText("张三");
        etName.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        EditText etPhone = new EditText(this);
        etPhone.setHint("电话");
        etPhone.setText("13800001111");
        etPhone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        EditText etDetail = new EditText(this);
        etDetail.setHint("详细地址");

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp(20), 0, dp(20), 0);
        container.addView(etName);
        container.addView(etPhone);
        container.addView(etDetail);

        // 用自定义点击替代默认的 setPositiveButton：校验失败时保留输入，不自动关闭
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("新增地址")
                .setView(container)
                .setPositiveButton("保存", null)
                .setNegativeButton("取消", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String name = etName.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String detail = etDetail.getText().toString().trim();
                    if (name.isEmpty()) {
                        etName.setError("请填写联系人");
                        etName.requestFocus();
                        return;
                    }
                    if (!phone.matches("1[3-9]\\d{9}")) {
                        etPhone.setError("请输入正确的 11 位手机号");
                        etPhone.requestFocus();
                        return;
                    }
                    if (detail.isEmpty()) {
                        etDetail.setError("地址不能为空");
                        etDetail.requestFocus();
                        return;
                    }
                    store.addAddress(new Address(0, name, phone, detail, false));
                    refresh();
                    dialog.dismiss();
                }));
        dialog.show();
    }

    private void refresh() {
        adapter.setData(store.getAddresses());
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}

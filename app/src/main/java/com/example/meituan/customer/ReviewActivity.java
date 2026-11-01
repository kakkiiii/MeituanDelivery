package com.example.meituan.customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.Order;
import com.example.meituan.model.Review;

/**
 * 我的评价：可查看评价列表；带 order_id 时先弹出评价框
 */
public class ReviewActivity extends AppCompatActivity {

    private DataStore store;
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        store = DataStore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new ReviewAdapter(this);
        RecyclerView recycler = findViewById(R.id.recycler_review);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        refresh();

        int orderId = getIntent().getIntExtra("order_id", -1);
        if (orderId != -1) {
            Order order = store.getOrder(orderId);
            if (order != null) showReviewDialog(order);
        }
    }

    private void showReviewDialog(Order order) {
        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_review, null);
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        EditText etContent = view.findViewById(R.id.et_content);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        view.findViewById(R.id.btn_submit).setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            if (rating < 1) {
                Toast.makeText(this, "请先打分（至少 1 星）", Toast.LENGTH_SHORT).show();
                return;
            }
            String content = etContent.getText().toString().trim();
            if (content.isEmpty()) content = "用户未填写评价";

            Review review = new Review(0, order.id, order.restaurant.name, rating,
                    content, DataStore.now());
            store.addReview(review);
            order.review = review;
            store.completeOrder(order);
            dialog.dismiss();
            Toast.makeText(this, "评价成功，感谢反馈！", Toast.LENGTH_SHORT).show();
            refresh();
            finish();
        });

        dialog.show();
    }

    private void refresh() {
        adapter.setData(store.getReviews());
    }
}

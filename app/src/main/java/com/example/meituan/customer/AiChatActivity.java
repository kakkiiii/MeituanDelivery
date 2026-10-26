package com.example.meituan.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meituan.R;
import com.example.meituan.data.DataStore;
import com.example.meituan.model.ChatMessage;
import com.example.meituan.model.Dish;
import com.example.meituan.model.Restaurant;
import com.example.meituan.util.DeepSeekClient;
import com.example.meituan.util.LocalAdvisor;

import java.util.Locale;

/**
 * AI 点餐助手：接入 DeepSeek API 推荐真实商家/菜品，未配置时本地降级推荐。
 */
public class AiChatActivity extends AppCompatActivity implements ChatAdapter.OnGoShop {

    private static final String[] QUICK = {"我饿了，吃点啥", "想吃辣，来个重口的", "想喝奶茶",
            "来点清淡的", "夜宵吃什么", "预算20元"};

    private DataStore store;
    private ChatAdapter adapter;
    private EditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        store = DataStore.getInstance();

        TextView tvMode = findViewById(R.id.tv_mode);
        boolean configured = DeepSeekClient.isConfigured();
        tvMode.setText(configured ? "DeepSeek 驱动 · 基于平台真实商家推荐"
                : "本地推荐模式（配置 DEEPSEEK_API_KEY 后启用 AI）");

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // 快捷问题
        LinearLayout quick = findViewById(R.id.quick_container);
        for (String q : QUICK) {
            TextView chip = new TextView(this);
            chip.setText(q);
            chip.setTextSize(13);
            chip.setTextColor(0xFF5A4400);
            chip.setBackgroundResource(R.drawable.bg_tag);
            chip.setPadding(dp(14), dp(8), dp(14), dp(8));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(dp(8));
            chip.setLayoutParams(lp);
            chip.setOnClickListener(v -> ask(q));
            quick.addView(chip);
        }

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setItemAnimator(null);
        adapter = new ChatAdapter(this);
        recycler.setAdapter(adapter);

        etInput = findViewById(R.id.et_input);
        findViewById(R.id.btn_send).setOnClickListener(v -> {
            String text = etInput.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "先告诉我想吃啥~", Toast.LENGTH_SHORT).show();
                return;
            }
            etInput.setText("");
            ask(text);
        });

        // 欢迎语
        adapter.add(ChatMessage.bot("你好呀，我是你的专属点餐助手 🍔\n"
                + "告诉我你的口味、预算或场景（比如\"想吃辣的、预算30\"），"
                + "我会从平台真实商家里帮你挑一个最合适的菜。"));
        scrollBottom();
    }

    private void ask(String userText) {
        adapter.add(ChatMessage.user(userText));
        adapter.add(ChatMessage.thinking());
        scrollBottom();

        if (DeepSeekClient.isConfigured()) {
            DeepSeekClient.ask(userText, new DeepSeekClient.Callback() {
                @Override
                public void onSuccess(@NonNull String result, boolean fromAi) {
                    showRecommend(result, true);
                }

                @Override
                public void onError(String message) {
                    showRecommend(LocalAdvisor.recommend(userText), false);
                }
            });
        } else {
            // 本地模式：短暂延迟模拟思考过程
            new android.os.Handler(getMainLooper()).postDelayed(
                    () -> showRecommend(LocalAdvisor.recommend(userText), false), 600);
        }
    }

    /** result 格式：restaurantId|dishId|dishName|reason */
    private void showRecommend(String result, boolean fromAi) {
        if (result == null) {
            adapter.replaceLast(ChatMessage.bot("抱歉，这次没挑选到合适的菜品，换个说法试试？"));
            scrollBottom();
            return;
        }
        String[] p = result.split("\\|", -1);
        int rid = Integer.parseInt(p[0]);
        int did = Integer.parseInt(p[1]);
        String dishName = p.length > 2 ? p[2] : "";
        String reason = p.length > 3 ? p[3] : "";

        Restaurant r = store.getRestaurant(rid);
        Dish d = store.getDish(did);
        if (r == null || d == null) {
            adapter.replaceLast(ChatMessage.bot("抱歉，没找到匹配的菜品，换个说法试试？"));
            scrollBottom();
            return;
        }

        ChatMessage m = ChatMessage.bot(fromAi
                ? "就这个，很适合你！"
                : "本地为你推荐（配置 DeepSeek Key 后可获得 AI 智能推荐）：");
        m.recommend = true;
        m.fromAi = fromAi;
        m.restaurantId = rid;
        m.dishId = did;
        m.dishName = d.name;
        m.dishPrice = String.format(Locale.CHINA, "%.2f", d.price);
        m.restaurantName = r.name + " · " + r.category;
        m.reason = reason.isEmpty() ? "招牌好菜，别错过" : reason;
        m.dishImage = d.image;
        adapter.replaceLast(m);
        scrollBottom();
    }

    @Override
    public void onGo(ChatMessage m) {
        startActivity(new Intent(this, RestaurantActivity.class)
                .putExtra("restaurant_id", m.restaurantId));
    }

    private void scrollBottom() {
        RecyclerView recycler = findViewById(R.id.recycler);
        if (adapter.getItemCount() > 0) {
            recycler.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }
}

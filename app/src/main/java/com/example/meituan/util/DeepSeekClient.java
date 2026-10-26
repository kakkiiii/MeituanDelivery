package com.example.meituan.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.meituan.BuildConfig;
import com.example.meituan.data.DataStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

/**
 * DeepSeek AI 点餐助手（真实接入 DeepSeek API）。
 *
 * 配置方法：在 local.properties 中增加一行
 *   DEEPSEEK_API_KEY=sk-xxxxxxxx
 * 未配置时自动降级为本地智能推荐。
 */
public class DeepSeekClient {

    private static final String TAG = "DeepSeek";
    private static final String API_URL = "https://api.deepseek.com/chat/completions";
    private static final String MODEL = "deepseek-chat";

    public interface Callback {
        /** result 为 "restaurantId|dishId|dishName|reason"，失败返回 null */
        void onSuccess(@NonNull String result, boolean fromAi);

        void onError(String message);
    }

    public static boolean isConfigured() {
        String key = BuildConfig.DEEPSEEK_API_KEY;
        return key != null && !key.isEmpty() && key.startsWith("sk-");
    }

    /**
     * 询问 AI 吃什么。userInput 为用户自然语言，callback 主线程回调。
     */
    public static void ask(String userInput, Callback callback) {
        final String menuContext = DataStore.getInstance().aiMenuContext();
        final String systemPrompt = buildSystemPrompt(menuContext);
        final String key = BuildConfig.DEEPSEEK_API_KEY;

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("model", MODEL);
                body.put("temperature", 0.8);
                body.put("max_tokens", 400);
                JSONArray messages = new JSONArray();
                messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));
                messages.put(new JSONObject().put("role", "user").put("content", userInput));
                body.put("messages", messages);

                String resp = post(API_URL, key, body.toString());
                JSONObject json = new JSONObject(resp);
                String content = json.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
                String parsed = parseRecommendation(content);
                if (parsed == null) {
                    post(callback, null, false);
                } else {
                    post(callback, parsed, true);
                }
            } catch (Exception e) {
                Log.w(TAG, "ask failed: " + e.getMessage());
                post(callback, null, false);
            }
        });
    }

    /** 构建包含真实商家/菜品清单的系统提示词 */
    private static String buildSystemPrompt(String menuContext) {
        return "你是外卖平台的AI点餐助手，下面是平台真实商家与菜品数据（格式：[商家id]店名|品类|起送价元；菜品: 菜品id.菜名|价格元）：\n"
                + menuContext
                + "\n规则：\n"
                + "1. 只能从上表中选择一个真实存在的菜品推荐，禁止编造不存在的菜品或店名\n"
                + "2. 结合用户需求（口味、预算、场景、人数、天气等）选出最合适的一个\n"
                + "3. 只输出一行JSON，不要任何多余文字：{\"restaurantId\":商家id,\"dishId\":菜品id,\"dishName\":\"菜品名\",\"reason\":\"20字以内的推荐理由\"}";
    }

    /** 从模型输出中提取推荐 JSON，返回 "restaurantId|dishId|dishName|reason" */
    private static String parseRecommendation(String content) {
        if (content == null) return null;
        String json = content.trim();
        // 去掉 ```json ... ``` 围栏
        int start = json.indexOf('{');
        int end = json.lastIndexOf('}');
        if (start < 0 || end <= start) return null;
        json = json.substring(start, end + 1);
        try {
            JSONObject o = new JSONObject(json);
            long rid = o.getLong("restaurantId");
            long did = o.getLong("dishId");
            String name = o.getString("dishName");
            String reason = o.optString("reason", "");
            // 校验必须是真实存在的商家/菜品
            if (DataStore.getInstance().getRestaurant((int) rid) == null
                    || DataStore.getInstance().getDish((int) did) == null) {
                return null;
            }
            return rid + "|" + did + "|" + name + "|" + reason;
        } catch (JSONException e) {
            return null;
        }
    }

    private static String post(String urlStr, String key, String body) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(60000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + key);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        conn.setFixedLengthStreamingMode(bytes.length);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(bytes);
        }
        int code = conn.getResponseCode();
        InputStream is = code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream();
        StringBuilder sb = new StringBuilder();
        if (is != null) {
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) sb.append(line);
            }
        }
        if (code < 200 || code >= 300) {
            throw new Exception("HTTP " + code + ": " + sb.substring(0, Math.min(200, sb.length())));
        }
        return sb.toString();
    }

    private static void post(Callback cb, String result, boolean fromAi) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (result != null) cb.onSuccess(result, fromAi);
            else cb.onError("AI 暂时无法推荐，已为你本地推荐");
        });
    }
}

package com.example.meituan.model;

/**
 * AI 聊天消息：文本消息或推荐卡片消息
 */
public class ChatMessage {
    public String text;            // 文本内容
    public boolean fromUser;       // 是否用户发送
    public boolean recommend;      // 是否为推荐消息（带菜品卡片）
    public boolean fromAi;         // 推荐来源：true=DeepSeek AI，false=本地推荐
    public boolean thinking;       // 是否"思考中"占位消息

    // 推荐内容
    public int restaurantId;
    public int dishId;
    public String dishName;
    public String dishPrice;
    public String restaurantName;
    public String reason;
    public String dishImage;

    public static ChatMessage user(String text) {
        ChatMessage m = new ChatMessage();
        m.text = text;
        m.fromUser = true;
        return m;
    }

    public static ChatMessage bot(String text) {
        ChatMessage m = new ChatMessage();
        m.text = text;
        m.fromUser = false;
        return m;
    }

    public static ChatMessage thinking() {
        ChatMessage m = new ChatMessage();
        m.text = "正在为你挑选美味…";
        m.thinking = true;
        return m;
    }
}

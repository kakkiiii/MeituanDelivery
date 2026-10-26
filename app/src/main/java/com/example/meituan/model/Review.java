package com.example.meituan.model;

/**
 * 评价模型
 */
public class Review {
    public int id;
    public int orderId;
    public String restaurantName;
    public int rating;        // 1-5 星
    public String content;
    public String time;

    public Review(int id, int orderId, String restaurantName, int rating, String content, String time) {
        this.id = id;
        this.orderId = orderId;
        this.restaurantName = restaurantName;
        this.rating = rating;
        this.content = content;
        this.time = time;
    }
}

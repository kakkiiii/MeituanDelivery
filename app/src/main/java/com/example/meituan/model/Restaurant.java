package com.example.meituan.model;

/**
 * 店铺模型
 */
public class Restaurant {
    public int id;
    public String name;
    public double rating;         // 评分
    public int monthlySales;      // 月售
    public double deliveryFee;    // 配送费
    public int deliveryTime;      // 配送时长（分钟）
    public double minPrice;       // 起送价
    public double distance;       // 距离（km）
    public String category;       // 分类（快餐/奶茶/正餐...）
    public String notice;         // 公告
    public boolean open;          // 是否营业
    public int imageColor;        // 颜色占位

    public Restaurant(int id, String name, double rating, int monthlySales, double deliveryFee,
                      int deliveryTime, double minPrice, double distance, String category,
                      String notice, boolean open, int imageColor) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.monthlySales = monthlySales;
        this.deliveryFee = deliveryFee;
        this.deliveryTime = deliveryTime;
        this.minPrice = minPrice;
        this.distance = distance;
        this.category = category;
        this.notice = notice;
        this.open = open;
        this.imageColor = imageColor;
    }
}

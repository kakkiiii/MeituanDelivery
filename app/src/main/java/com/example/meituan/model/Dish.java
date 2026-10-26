package com.example.meituan.model;

/**
 * 菜品模型
 */
public class Dish {
    public int id;
    public int restaurantId;
    public String name;
    public double price;          // 单价（元）
    public String description;
    public int monthlySales;      // 月售
    public String category;       // 分类
    public boolean available;     // 是否在售
    public int imageColor;        // 用颜色占位代替真实图片

    public Dish(int id, int restaurantId, String name, double price, String description,
                int monthlySales, String category, boolean available, int imageColor) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.monthlySales = monthlySales;
        this.category = category;
        this.available = available;
        this.imageColor = imageColor;
    }
}

package com.example.meituan.model;

/**
 * 优惠券模型
 */
public class Coupon {
    public int id;
    public String title;
    public double amount;      // 面额
    public double threshold;   // 使用门槛（满 X 可用）
    public boolean used;

    public Coupon(int id, String title, double amount, double threshold) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.threshold = threshold;
        this.used = false;
    }
}

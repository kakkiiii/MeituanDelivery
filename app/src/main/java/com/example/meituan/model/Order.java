package com.example.meituan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单模型：贯穿 顾客端 -> 商家端 的状态流转核心
 */
public class Order implements Serializable {

    // 订单状态常量
    public static final int STATUS_PENDING = 0;          // 待商家接单（顾客已支付）
    public static final int STATUS_MERCHANT_ACCEPTED = 1; // 商家已接单/备餐中
    public static final int STATUS_READY = 2;             // 出餐完成，配送中
    public static final int STATUS_DELIVERED = 3;         // 已送达，待评价
    public static final int STATUS_COMPLETED = 4;         // 已完成（已评价）
    public static final int STATUS_CANCELED = 5;          // 已取消/已拒绝

    public int id;
    public String orderNo;
    public Restaurant restaurant;
    public List<OrderItem> items = new ArrayList<>();
    public double totalPrice;        // 商品总价
    public double deliveryFee;
    public double couponAmount;      // 优惠金额
    public double payable;           // 实付金额
    public int status;
    public Address address;
    public String remark;
    public String createTime;
    public Review review;

    // 配送环节模拟字段
    public String deliverTime = "";

    public Order() {
    }

    public static class OrderItem implements Serializable {
        public Dish dish;
        public int count;

        public OrderItem(Dish dish, int count) {
            this.dish = dish;
            this.count = count;
        }

        public double subTotal() {
            return dish.price * count;
        }
    }

    public static String statusText(int status) {
        switch (status) {
            case STATUS_PENDING: return "待接单";
            case STATUS_MERCHANT_ACCEPTED: return "备餐中";
            case STATUS_READY: return "配送中";
            case STATUS_DELIVERED: return "待评价";
            case STATUS_COMPLETED: return "已完成";
            case STATUS_CANCELED: return "已取消";
            default: return "未知";
        }
    }

    public String itemsSummary() {
        StringBuilder sb = new StringBuilder();
        for (OrderItem it : items) {
            if (sb.length() > 0) sb.append("、");
            sb.append(it.dish.name).append(" x").append(it.count);
        }
        return sb.toString();
    }
}

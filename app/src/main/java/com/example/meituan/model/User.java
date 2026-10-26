package com.example.meituan.model;

/**
 * 用户模型（顾客 / 商家）
 */
public class User {
    public int id;
    public String name;
    public String phone;
    public String role; // customer / merchant

    public User(int id, String name, String phone, String role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }
}

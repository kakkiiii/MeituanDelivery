package com.example.meituan.model;

/**
 * 地址模型
 */
public class Address {
    public int id;
    public String name;       // 联系人
    public String phone;
    public String detail;     // 详细地址
    public boolean isDefault;

    public Address(int id, String name, String phone, String detail, boolean isDefault) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.detail = detail;
        this.isDefault = isDefault;
    }

    public String full() {
        return detail + "（" + name + " " + phone + "）";
    }
}

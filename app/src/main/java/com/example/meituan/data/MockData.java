package com.example.meituan.data;

import com.example.meituan.model.Address;
import com.example.meituan.model.Coupon;
import com.example.meituan.model.Dish;
import com.example.meituan.model.Restaurant;
import com.example.meituan.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地 Mock 数据：店铺、菜品、地址、优惠券、用户
 */
public class MockData {

    public static final int C_RED = 0xFFEE6A5F;
    public static final int C_ORANGE = 0xFFF5A623;
    public static final int C_GREEN = 0xFF7ED321;
    public static final int C_BLUE = 0xFF4A90D9;
    public static final int C_PURPLE = 0xFF9013FE;
    public static final int C_TEAL = 0xFF50E3C2;
    public static final int C_PINK = 0xFFFF6B81;
    public static final int C_BROWN = 0xFF8B572A;

    private static int dishId = 1;

    public static List<Restaurant> restaurants() {
        List<Restaurant> list = new ArrayList<>();
        list.add(new Restaurant(1, "川香麻辣烫", 4.7, 3286, 3.0, 30, 20.0, 1.2, "麻辣烫", "本店使用新鲜食材，麻辣鲜香", true, C_RED));
        list.add(new Restaurant(2, "肯德基", 4.5, 5210, 5.0, 35, 30.0, 2.1, "快餐", "疯狂星期四，欢迎下单", true, C_ORANGE));
        list.add(new Restaurant(3, "蜜雪冰城", 4.8, 8900, 2.0, 25, 8.0, 0.8, "奶茶", "甜蜜蜜，你爱我我爱你", true, C_TEAL));
        list.add(new Restaurant(4, "兰州拉面", 4.6, 2100, 3.0, 28, 18.0, 1.5, "面食", "一清二白三红四绿", true, C_GREEN));
        list.add(new Restaurant(5, "必胜客", 4.4, 1800, 6.0, 40, 50.0, 3.0, "披萨", "欢乐餐厅，全家共享", true, C_PURPLE));
        list.add(new Restaurant(6, "沙县小吃", 4.3, 4300, 2.0, 20, 15.0, 0.9, "快餐", "拌面扁肉，经济实惠", true, C_BROWN));
        list.add(new Restaurant(7, "一点点", 4.6, 6500, 2.5, 25, 10.0, 1.1, "奶茶", "波霸奶茶，招牌必点", true, C_PINK));
        list.add(new Restaurant(8, "黄焖鸡米饭", 4.5, 2800, 3.0, 30, 22.0, 1.8, "快餐", "招牌黄焖鸡，肉嫩汤浓", false, C_BLUE));
        return list;
    }

    public static List<Dish> dishes() {
        List<Dish> list = new ArrayList<>();

        // 川香麻辣烫
        list.add(d(1, "招牌麻辣烫", 19.9, "骨汤熬制，麻辣鲜香", 1200, "招牌", true, C_RED));
        list.add(d(1, "番茄麻辣烫", 21.9, "浓郁番茄汤底", 800, "招牌", true, C_RED));
        list.add(d(1, "肥牛卷", 12.0, "精选肥牛，嫩滑可口", 600, "配菜", true, C_ORANGE));
        list.add(d(1, "手工牛肉丸", 8.0, "Q弹有嚼劲", 500, "配菜", true, C_ORANGE));
        list.add(d(1, "冰镇酸梅汤", 5.0, "解辣必备", 400, "饮品", true, C_TEAL));

        // 肯德基
        list.add(d(2, "香辣鸡腿堡", 19.5, "经典香辣，外酥里嫩", 2200, "招牌", true, C_ORANGE));
        list.add(d(2, "奥尔良烤翅", 13.5, "一对装，香嫩多汁", 1800, "小食", true, C_ORANGE));
        list.add(d(2, "薯条(大)", 12.5, "外脆内软", 1500, "小食", true, C_ORANGE));
        list.add(d(2, "可乐(中)", 9.0, "冰爽畅快", 1600, "饮品", true, C_BLUE));
        list.add(d(2, "全家桶", 88.0, "适合3-4人分享", 500, "套餐", true, C_ORANGE));

        // 蜜雪冰城
        list.add(d(3, "冰鲜柠檬水", 4.0, "招牌人气饮品", 4200, "招牌", true, C_TEAL));
        list.add(d(3, "摇摇奶昔", 8.0, "多种口味可选", 2500, "奶茶", true, C_TEAL));
        list.add(d(3, "珍珠奶茶", 7.0, "Q弹珍珠", 3000, "奶茶", true, C_TEAL));
        list.add(d(3, "冰淇淋", 3.0, "香草口味", 2800, "甜品", true, C_PINK));

        // 兰州拉面
        list.add(d(4, "牛肉拉面", 16.0, "手工拉制，汤清味浓", 1300, "招牌", true, C_GREEN));
        list.add(d(4, "炒拉条", 18.0, "大火爆炒，香气扑鼻", 700, "面食", true, C_GREEN));
        list.add(d(4, "牛肉拌面", 17.0, "酱香浓郁", 600, "面食", true, C_GREEN));
        list.add(d(4, "茶叶蛋", 2.5, "入味十足", 800, "配菜", true, C_BROWN));

        // 必胜客
        list.add(d(5, "超级至尊披萨", 69.0, "多种配料，满足味蕾", 900, "披萨", true, C_PURPLE));
        list.add(d(5, "夏威夷风光披萨", 59.0, "火腿+菠萝，清爽", 700, "披萨", true, C_PURPLE));
        list.add(d(5, "意大利肉酱面", 32.0, "经典意式风味", 800, "主食", true, C_RED));
        list.add(d(5, "芝士焗薯泥", 18.0, "奶香浓郁", 500, "小食", true, C_ORANGE));

        // 沙县小吃
        list.add(d(6, "拌面", 6.0, "花生酱香浓", 2000, "招牌", true, C_BROWN));
        list.add(d(6, "扁肉(馄饨)", 8.0, "皮薄馅嫩", 1800, "招牌", true, C_BROWN));
        list.add(d(6, "蒸饺", 9.0, "一笼十个", 1500, "小食", true, C_BROWN));
        list.add(d(6, "炖罐汤", 12.0, "多种汤品可选", 900, "汤品", true, C_BROWN));

        // 一点点
        list.add(d(7, "波霸奶茶", 12.0, "招牌波霸，口感丰富", 3200, "招牌", true, C_PINK));
        list.add(d(7, "四季春茶", 9.0, "清爽回甘", 1500, "纯茶", true, C_PINK));
        list.add(d(7, "珍珠鲜奶", 14.0, "鲜奶+黑糖珍珠", 2000, "奶茶", true, C_PINK));
        list.add(d(7, "冰淇淋红茶", 13.0, "红茶+香草冰淇淋", 1600, "奶茶", true, C_PINK));

        // 黄焖鸡米饭
        list.add(d(8, "招牌黄焖鸡", 22.0, "鸡肉鲜嫩，汤汁浓郁", 1500, "招牌", true, C_BLUE));
        list.add(d(8, "微辣黄焖鸡", 22.0, "微辣口味", 1000, "招牌", true, C_BLUE));
        list.add(d(8, "加鸡腿", 6.0, "加量更满足", 700, "配菜", true, C_BLUE));
        list.add(d(8, "米饭", 2.0, "东北大米", 2000, "主食", true, C_GREEN));

        return list;
    }

    private static Dish d(int rid, String name, double price, String desc,
                          int sales, String cat, boolean av, int color) {
        return new Dish(dishId++, rid, name, price, desc, sales, cat, av, color);
    }

    public static List<Address> addresses() {
        List<Address> list = new ArrayList<>();
        list.add(new Address(1, "张三", "13800001111", "北京市朝阳区望京SOHO T1 座 8 层", true));
        list.add(new Address(2, "张三", "13800001111", "北京市海淀区中关村软件园 10 号楼", false));
        list.add(new Address(3, "李四", "13900002222", "北京市朝阳区三里屯太古里南区", false));
        return list;
    }

    public static List<Coupon> coupons() {
        List<Coupon> list = new ArrayList<>();
        list.add(new Coupon(1, "新客立减券", 8.0, 25.0));
        list.add(new Coupon(2, "满减券", 15.0, 60.0));
        list.add(new Coupon(3, "配送费减免券", 3.0, 0.0));
        list.add(new Coupon(4, "大额满减券", 20.0, 100.0));
        return list;
    }

    public static User customer() {
        return new User(1, "张三", "13800001111", "customer");
    }

    public static User merchant() {
        return new User(2, "川香麻辣烫老板", "13700003333", "merchant");
    }
}

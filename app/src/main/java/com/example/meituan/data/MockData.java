package com.example.meituan.data;

import com.example.meituan.model.Address;
import com.example.meituan.model.Coupon;
import com.example.meituan.model.Dish;
import com.example.meituan.model.Restaurant;
import com.example.meituan.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地 Mock 数据：10 家店铺、每家 30 个真实菜品，共 300 个菜品。
 * 菜品照片为真实食物图片，存放于 assets/food/，按菜名关键词自动匹配。
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

    private static final int[] COLORS = {C_RED, C_ORANGE, C_TEAL, C_GREEN, C_PURPLE,
            C_BROWN, C_PINK, C_BLUE, C_ORANGE, C_GREEN};

    private static int dishId = 1;
    private static final Map<String, Integer> imgCursor = new HashMap<>();

    /** 各类真实图片的张数（与 assets/food/manifest.json 对应） */
    private static final Map<String, Integer> IMG_COUNT = new HashMap<>();
    static {
        IMG_COUNT.put("chicken", 8);
        IMG_COUNT.put("beef", 6);
        IMG_COUNT.put("pork", 6);
        IMG_COUNT.put("fish", 6);
        IMG_COUNT.put("noodle", 6);
        IMG_COUNT.put("rice", 5);
        IMG_COUNT.put("dessert", 6);
        IMG_COUNT.put("salad", 5);
        IMG_COUNT.put("pizza", 3);
        IMG_COUNT.put("soup", 4);
        IMG_COUNT.put("breakfast", 3);
        IMG_COUNT.put("drink", 8);
    }

    public static List<Restaurant> restaurants() {
        List<Restaurant> list = new ArrayList<>();
        list.add(new Restaurant(1, "川香麻辣烫", 4.7, 3286, 3.0, 30, 20.0, 1.2, "麻辣烫", "本店使用新鲜食材，麻辣鲜香", true, C_RED, "food/soup_1.jpg"));
        list.add(new Restaurant(2, "肯德基", 4.5, 5210, 5.0, 35, 30.0, 2.1, "快餐", "疯狂星期四，欢迎下单", true, C_ORANGE, "food/chicken_1.jpg"));
        list.add(new Restaurant(3, "蜜雪冰城", 4.8, 8900, 2.0, 25, 8.0, 0.8, "奶茶", "甜蜜蜜，你爱我我爱你", true, C_TEAL, "food/drink_1.jpg"));
        list.add(new Restaurant(4, "兰州拉面", 4.6, 2100, 3.0, 28, 18.0, 1.5, "面食", "一清二白三红四绿", true, C_GREEN, "food/noodle_1.jpg"));
        list.add(new Restaurant(5, "必胜客", 4.4, 1800, 6.0, 40, 50.0, 3.0, "披萨", "欢乐餐厅，全家共享", true, C_PURPLE, "food/pizza_1.jpg"));
        list.add(new Restaurant(6, "沙县小吃", 4.3, 4300, 2.0, 20, 15.0, 0.9, "快餐", "拌面扁肉，经济实惠", true, C_BROWN, "food/noodle_2.jpg"));
        list.add(new Restaurant(7, "一点点", 4.6, 6500, 2.5, 25, 10.0, 1.1, "奶茶", "波霸奶茶，招牌必点", true, C_PINK, "food/drink_2.jpg"));
        list.add(new Restaurant(8, "黄焖鸡米饭", 4.5, 2800, 3.0, 30, 22.0, 1.8, "快餐", "招牌黄焖鸡，肉嫩汤浓", true, C_BLUE, "food/chicken_2.jpg"));
        list.add(new Restaurant(9, "木屋烧烤", 4.6, 3600, 4.0, 45, 35.0, 2.5, "烧烤", "炭火现烤，夜宵首选", true, C_ORANGE, "food/beef_1.jpg"));
        list.add(new Restaurant(10, "轻食主义", 4.7, 1500, 3.5, 30, 25.0, 1.6, "轻食", "低脂低卡，健康每一天", true, C_GREEN, "food/salad_1.jpg"));
        return list;
    }

    public static List<Dish> dishes() {
        List<Dish> list = new ArrayList<>();

        // 1 川香麻辣烫（30）
        add(list, 1, "招牌", "soup", "招牌麻辣烫@19.9", "番茄麻辣烫@21.9", "骨汤麻辣烫@18.9", "麻辣拌@17.9", "酸辣粉@12.9", "冒菜@20.9");
        add(list, 1, "荤菜", "soup", "肥牛卷@12.0", "手工牛肉丸@8.0", "午餐肉@7.0", "蟹柳@6.0", "鱼豆腐@5.0", "鹌鹑蛋@6.0", "鸭血@4.0", "小酥肉@10.0");
        add(list, 1, "素菜", "salad", "金针菇@4.0", "娃娃菜@3.5", "土豆片@3.0", "藕片@4.0", "海带结@3.5", "油麦菜@3.5", "豆腐泡@4.0", "宽粉@4.5");
        add(list, 1, "主食", "noodle", "方便面@3.0", "米饭@2.0", "红薯粉@4.0", "年糕@5.0");
        add(list, 1, "饮品", "drink", "冰镇酸梅汤@5.0", "冰豆浆@4.0", "可乐@3.5", "柠檬茶@6.0");

        // 2 肯德基（30）
        add(list, 2, "汉堡", "chicken", "香辣鸡腿堡@19.5", "奥尔良鸡腿堡@20.5", "劲脆鸡腿堡@19.5", "深海鳕鱼堡@22.0", "老北京鸡肉卷@17.5", "墨西哥鸡肉卷@17.5");
        add(list, 2, "炸鸡", "chicken", "吮指原味鸡@13.0", "香辣鸡翅@12.5", "奥尔良烤翅@13.5", "劲爆鸡米花@12.0", "黄金鸡块@11.0", "脆皮炸鸡@15.0");
        add(list, 2, "小食", "chicken", "薯条(大)@12.5", "葡式蛋挞@8.0", "土豆泥@7.5", "玉米棒@6.0", "上校鸡块@11.5", "红豆派@7.0");
        add(list, 2, "饮品", "drink", "可乐(中)@9.0", "雪碧(中)@9.0", "九珍果汁@11.0", "冰拿铁@14.0", "热美式@12.0", "柠檬红茶@10.0");
        add(list, 2, "套餐", "chicken", "全家桶@88.0", "欢乐双人餐@69.0", "单人汉堡餐@32.0", "儿童快乐餐@28.0", "早餐帕尼尼餐@18.0", "下午茶组合@25.0");

        // 3 蜜雪冰城（30）
        add(list, 3, "招牌", "drink", "冰鲜柠檬水@4.0", "高山四季春@5.0", "茉莉绿茶@5.0", "冰美式@6.0", "雪顶咖啡@8.0");
        add(list, 3, "奶茶", "drink", "珍珠奶茶@7.0", "椰椰奶茶@9.0", "红豆奶茶@8.0", "布丁奶茶@8.0", "燕麦奶茶@9.0", "三拼霸霸奶茶@10.0");
        add(list, 3, "果茶", "drink", "满杯百香果@9.0", "棒打鲜橙@8.0", "草莓啵啵@10.0", "蜜桃四季春@9.0", "葡萄冻冻@10.0", "杨枝甘露@12.0");
        add(list, 3, "冰淇淋", "dessert", "魔天脆脆@3.0", "草莓圣代@7.0", "奥利奥圣代@8.0", "黑糖珍珠圣代@8.0", "抹茶圣代@8.0");
        add(list, 3, "奶昔", "drink", "草莓奶昔@8.0", "芒果奶昔@8.0", "蓝莓奶昔@8.0", "巧克力奶昔@9.0");
        add(list, 3, "咖啡", "drink", "拿铁咖啡@9.0", "卡布奇诺@10.0", "焦糖拿铁@11.0", "椰椰冰萃@10.0");

        // 4 兰州拉面（30）
        add(list, 4, "拉面", "noodle", "牛肉拉面@16.0", "清汤牛肉面@17.0", "红烧牛肉面@20.0", "酸菜牛肉面@18.0", "毛细牛肉面@17.0", "二细牛肉面@17.0");
        add(list, 4, "拌面", "noodle", "牛肉拌面@17.0", "葱油拌面@10.0", "炸酱面@14.0", "麻酱拌面@13.0");
        add(list, 4, "炒面", "noodle", "炒拉条@18.0", "炒刀削@18.0", "牛肉炒面@20.0", "鸡蛋炒面@14.0");
        add(list, 4, "盖饭", "rice", "孜然牛肉盖饭@22.0", "土豆牛肉盖饭@21.0", "鱼香肉丝盖饭@19.0", "宫保鸡丁盖饭@19.0");
        add(list, 4, "小菜", "salad", "茶叶蛋@2.5", "卤牛肉@15.0", "凉拌黄瓜@6.0", "凉拌木耳@6.0", "酸辣土豆丝@7.0", "虎皮青椒@7.0");
        add(list, 4, "汤", "soup", "牛肉汤@10.0", "羊肉汤@15.0", "紫菜蛋花汤@5.0");
        add(list, 4, "饮品", "drink", "酸梅汤@5.0", "杏皮水@6.0", "老酸奶@6.0");

        // 5 必胜客（30）
        add(list, 5, "披萨", "pizza", "超级至尊披萨@69.0", "夏威夷风光披萨@59.0", "海鲜至尊披萨@79.0", "烤肉披萨@65.0", "榴莲披萨@72.0", "玛格丽特披萨@49.0", "黑椒牛肉披萨@69.0", "薯角培根披萨@62.0");
        add(list, 5, "意面", "noodle", "意大利肉酱面@32.0", "奶油培根意面@35.0", "海鲜意面@38.0", "黑椒牛柳意面@36.0");
        add(list, 5, "小食", "chicken", "芝士焗薯泥@18.0", "蜜汁鸡翅@22.0", "薯条@15.0", "鸡米花@18.0", "洋葱圈@15.0", "蒜香面包@12.0");
        add(list, 5, "沙拉", "salad", "凯撒沙拉@25.0", "田园沙拉@22.0", "金枪鱼沙拉@28.0");
        add(list, 5, "甜品", "dessert", "提拉米苏@26.0", "巧克力熔岩蛋糕@28.0", "抹茶雪域@24.0", "香草冰淇淋@16.0");
        add(list, 5, "饮品", "drink", "可乐@8.0", "柠檬红茶@12.0", "卡布奇诺@18.0", "鲜榨橙汁@16.0");
        add(list, 5, "主食", "rice", "照烧鸡肉饭@32.0");

        // 6 沙县小吃（30）
        add(list, 6, "招牌", "noodle", "拌面@6.0", "扁肉(馄饨)@8.0", "蒸饺@9.0", "炖罐@12.0", "卤鸭腿@10.0", "卤鸡腿@9.0");
        add(list, 6, "汤品", "soup", "排骨汤@12.0", "老鸭汤@13.0", "乌鸡汤@14.0", "猪肚汤@13.0", "鱼丸汤@9.0", "肉丸汤@9.0");
        add(list, 6, "饭类", "rice", "鸡腿饭@15.0", "鸭腿饭@16.0", "卤肉饭@14.0", "猪排饭@16.0", "牛腩饭@18.0", "蛋炒饭@10.0");
        add(list, 6, "面食", "noodle", "炸酱面@12.0", "牛肉面@15.0", "酸菜面@11.0", "葱油拌面@8.0");
        add(list, 6, "小食", "breakfast", "煎饺@9.0", "锅贴@9.0", "春卷@6.0", "茶叶蛋@2.0", "卤豆干@3.0");
        add(list, 6, "饮品", "drink", "绿豆汤@4.0", "豆浆@3.0", "酸梅汤@4.0");

        // 7 一点点（30）
        add(list, 7, "奶茶", "drink", "波霸奶茶@12.0", "珍珠奶茶@11.0", "椰果奶茶@12.0", "红豆奶茶@12.0", "布丁奶茶@13.0", "仙草奶茶@12.0");
        add(list, 7, "鲜奶", "drink", "珍珠鲜奶@14.0", "黑糖鲜奶@15.0", "抹茶拿铁@16.0", "红茶拿铁@15.0", "乌龙拿铁@15.0");
        add(list, 7, "纯茶", "drink", "四季春茶@9.0", "茉莉绿茶@9.0", "阿萨姆红茶@9.0", "冻顶乌龙@10.0", "蜂蜜绿茶@10.0");
        add(list, 7, "果茶", "drink", "柠檬茶@10.0", "百香果茶@12.0", "葡萄柚绿茶@13.0", "芒果青茶@13.0", "金桔柠檬@11.0");
        add(list, 7, "特色", "drink", "冰淇淋红茶@13.0", "奶盖绿茶@14.0", "奶盖红茶@14.0", "芝士乌龙@15.0", "可可芭蕾@15.0", "燕麦奶茶@14.0");
        add(list, 7, "咖啡", "drink", "美式咖啡@12.0", "拿铁咖啡@14.0", "焦糖玛奇朵@16.0");

        // 8 黄焖鸡米饭（30）
        add(list, 8, "招牌", "chicken", "招牌黄焖鸡(小份)@22.0", "招牌黄焖鸡(大份)@32.0", "微辣黄焖鸡@22.0", "特辣黄焖鸡@22.0", "黄焖排骨@28.0", "黄焖猪蹄@30.0");
        add(list, 8, "加料", "chicken", "加鸡腿@6.0", "加鸡翅@7.0", "加土豆@3.0", "加香菇@4.0", "加金针菇@4.0", "加豆腐@3.0", "加宽粉@4.0", "加青菜@3.0");
        add(list, 8, "主食", "rice", "米饭@2.0", "杂粮饭@3.0", "蛋炒饭@10.0", "腊肠炒饭@12.0");
        add(list, 8, "面食", "noodle", "黄焖鸡拌面@18.0", "鸡汤面@12.0", "酸菜肉丝面@14.0");
        add(list, 8, "汤", "soup", "紫菜蛋花汤@5.0", "番茄蛋汤@5.0", "冬瓜排骨汤@10.0");
        add(list, 8, "小菜", "salad", "凉拌黄瓜@5.0", "卤蛋@2.5", "爽口泡菜@4.0");
        add(list, 8, "饮品", "drink", "可乐@3.0", "雪碧@3.0", "酸梅汤@4.0");

        // 9 木屋烧烤（30）
        add(list, 9, "烤串", "beef", "羊肉串@5.0", "牛肉串@5.0", "五花肉串@4.0", "烤鸡翅@8.0", "鸡脆骨@5.0", "掌中宝@6.0", "烤腰子@10.0", "烤板筋@5.0");
        add(list, 9, "海鲜", "fish", "烤生蚝@12.0", "烤扇贝@12.0", "烤鱿鱼@10.0", "烤虾@8.0", "烤秋刀鱼@12.0");
        add(list, 9, "素菜", "salad", "烤茄子@8.0", "烤韭菜@6.0", "烤金针菇@6.0", "烤玉米@5.0", "烤土豆片@5.0", "烤馒头片@4.0");
        add(list, 9, "主食", "noodle", "炒方便面@12.0", "烤冷面@10.0", "蛋炒饭@12.0", "炒河粉@14.0");
        add(list, 9, "锡纸", "soup", "锡纸金针菇@12.0", "锡纸花甲@18.0", "锡纸娃娃菜@12.0", "锡纸豆腐@12.0");
        add(list, 9, "饮品", "drink", "啤酒@6.0", "可乐@4.0", "酸梅汤@5.0");

        // 10 轻食主义（30）
        add(list, 10, "沙拉", "salad", "鸡胸肉沙拉@26.0", "牛肉沙拉@32.0", "金枪鱼沙拉@30.0", "虾仁沙拉@32.0", "牛油果沙拉@28.0", "凯撒沙拉@25.0");
        add(list, 10, "主食", "rice", "糙米饭@12.0", "藜麦饭@15.0", "荞麦面@18.0", "全麦三明治@20.0", "低脂鸡肉卷@22.0", "牛肉卷@24.0");
        add(list, 10, "套餐", "salad", "低脂鸡胸餐@32.0", "增肌牛肉餐@38.0", "高蛋白虾仁餐@36.0", "素食藜麦餐@28.0", "轻体牛油果餐@30.0", "黑椒牛排餐@42.0");
        add(list, 10, "小食", "breakfast", "水煮蛋@3.0", "烤红薯@6.0", "玉米杯@6.0", "坚果酸奶杯@12.0", "时令水果杯@10.0");
        add(list, 10, "饮品", "drink", "美式咖啡@12.0", "无糖绿茶@6.0", "鲜榨橙汁@15.0", "柠檬气泡水@10.0", "奇亚籽酸奶@14.0", "脱脂牛奶@8.0");
        add(list, 10, "汤", "soup", "南瓜浓汤@12.0");

        return list;
    }

    /** 定义格式：菜名@价格，分类作为菜品分类，defType 为图片兜底类型 */
    private static void add(List<Dish> list, int rid, String cat, String defType, String... defs) {
        for (String def : defs) {
            String[] p = def.split("@");
            String name = p[0];
            double price = Double.parseDouble(p[1]);
            int sales = 150 + Math.floorMod(name.hashCode(), 2800);
            list.add(new Dish(dishId++, rid, name, price, descFor(name, cat), sales,
                    cat, true, COLORS[(rid - 1) % COLORS.length], img(name, defType)));
        }
    }

    private static String descFor(String name, String cat) {
        if ("招牌".equals(cat)) return "本店招牌，好评如潮";
        String[] pool = {"新鲜现做，量大实惠", "经典口味，值得一试", "精心制作，欢迎品尝", "人气单品，回购率高"};
        return pool[Math.floorMod(name.hashCode(), pool.length)];
    }

    /** 按菜名关键词匹配真实图片，同类型内轮换，避免大量重复 */
    private static String img(String name, String defType) {
        String t = defType;
        if (has(name, "茶", "咖啡", "可乐", "雪碧", "果汁", "豆浆", "酸梅汤", "杏皮水", "酸奶",
                "牛奶", "气泡水", "啤酒", "奶昔", "柠檬", "鲜橙", "百香果", "葡萄", "芒果",
                "蜜桃", "椰", "燕麦", "可可", "鲜奶", "拿铁", "美式", "玛奇朵", "波霸",
                "杨枝甘露", "奶盖", "红茶", "绿茶", "乌龙")) t = "drink";
        else if (has(name, "披萨")) t = "pizza";
        else if (has(name, "沙拉")) t = "salad";
        else if (has(name, "圣代", "冰淇淋", "脆脆", "蛋糕", "提拉米苏", "蛋挞", "派", "雪域", "熔岩")) t = "dessert";
        else if (has(name, "面", "粉", "拉条", "刀削", "河粉", "冷面", "乌冬")) t = "noodle";
        else if (has(name, "饭", "粥", "糙米", "藜麦")) t = "rice";
        else if (has(name, "鱼", "虾", "生蚝", "扇贝", "鱿鱼", "海鲜", "鳕鱼", "秋刀鱼", "花甲")) t = "fish";
        else if (has(name, "汤", "炖罐")) t = "soup";
        else if (has(name, "牛", "牛腩", "牛排", "羊肉", "腰子", "板筋")) t = "beef";
        else if (has(name, "猪", "排骨", "五花", "叉烧", "卤肉", "猪蹄", "猪肚", "腊肠", "午餐肉", "酥肉")) t = "pork";
        else if (has(name, "鸡", "鸭", "翅", "腿", "脆骨", "掌中宝", "汉堡", "帕尼尼")) t = "chicken";
        else if (has(name, "蛋", "饺", "春卷", "三明治", "面包", "红薯", "坚果", "馒头")) t = "breakfast";
        else if (has(name, "沙拉", "黄瓜", "木耳", "土豆", "茄子", "韭菜", "金针菇", "豆腐",
                "玉米", "青菜", "泡菜", "海带", "藕", "娃娃菜", "油麦菜", "水果", "青椒", "香菇")) t = "salad";

        Integer count = IMG_COUNT.get(t);
        if (count == null || count == 0) {
            t = "chicken";
            count = IMG_COUNT.get(t);
        }
        int idx = imgCursor.containsKey(t) ? imgCursor.get(t) : 0;
        imgCursor.put(t, idx + 1);
        return "food/" + t + "_" + (idx % count + 1) + ".jpg";
    }

    private static boolean has(String name, String... keys) {
        for (String k : keys) if (name.contains(k)) return true;
        return false;
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

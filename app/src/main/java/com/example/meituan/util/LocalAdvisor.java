package com.example.meituan.util;

import com.example.meituan.data.DataStore;
import com.example.meituan.model.Dish;
import com.example.meituan.model.Restaurant;

import java.util.List;
import java.util.Random;

/**
 * 本地智能推荐：DeepSeek 未配置或调用失败时的降级方案。
 * 先按用户输入匹配菜品名/店名/品类；无命中时按口味意图给店铺加权打分。
 */
public class LocalAdvisor {

    private static final Random RANDOM = new Random();

    /** 返回 "restaurantId|dishId|dishName|reason" */
    public static String recommend(String userInput) {
        DataStore store = DataStore.getInstance();
        String q = userInput == null ? "" : userInput.trim();
        List<Restaurant> restaurants = store.getRestaurants();

        // 1. 输入直接命中菜品名/店名/品类：优先推荐该店的招牌
        for (Restaurant r : restaurants) {
            if (!r.open) continue;
            boolean hit = r.name.contains(q) || r.category.contains(q);
            if (!hit) {
                for (Dish d : store.getDishesByRestaurant(r.id)) {
                    if (d.name.contains(q)) {
                        hit = true;
                        break;
                    }
                }
            }
            if (hit) {
                Dish signature = signatureDish(store, r);
                if (signature != null) {
                    return r.id + "|" + signature.id + "|" + signature.name + "|"
                            + reason(r, signature);
                }
            }
        }

        // 2. 按口味意图加权挑选店铺
        Restaurant target = pickRestaurant(restaurants, q);
        if (target == null) return null;
        Dish dish = signatureDish(store, target);
        if (dish == null) dish = pickDish(store, target);
        if (dish == null) return null;
        return target.id + "|" + dish.id + "|" + dish.name + "|" + reason(target, dish);
    }

    /** 挑店铺的招牌菜（无招牌分类时取月售最高） */
    private static Dish signatureDish(DataStore store, Restaurant r) {
        Dish best = null;
        int bestSales = -1;
        for (Dish d : store.getDishesByRestaurant(r.id)) {
            if ("招牌".equals(d.category)) return d;
            if (d.monthlySales > bestSales) {
                bestSales = d.monthlySales;
                best = d;
            }
        }
        return best;
    }

    private static Restaurant pickRestaurant(List<Restaurant> restaurants, String q) {
        Restaurant best = null;
        double bestScore = -1;
        for (Restaurant r : restaurants) {
            if (!r.open) continue;
            double score = r.rating * 60 + r.monthlySales / 100.0;
            String name = r.name;
            String cat = r.category;

            // 口味意图（权重高于评分，保证“想吃辣”真的推辣的）
            if (has(q, "辣", "重口", "麻辣", "火锅", "过瘾", "香锅"))
                score += (cat.equals("麻辣烫") || cat.equals("烧烤")) ? 220 : 0;
            if (has(q, "清淡", "健康", "减肥", "轻食", "低脂", "素"))
                score += cat.equals("轻食") ? 220 : 0;
            if (has(q, "奶茶", "喝", "甜的", "饮品", "饮料", "咖啡"))
                score += cat.equals("奶茶") ? 220 : 0;
            if (has(q, "面", "拉面", "粉"))
                score += cat.equals("面食") ? 200 : 0;
            if (has(q, "披萨", "西餐", "意面"))
                score += cat.equals("披萨") ? 220 : 0;
            if (has(q, "鸡", "汉堡", "炸鸡", "快餐"))
                score += name.contains("肯德基") ? 200 : (cat.equals("快餐") ? 60 : 0);
            if (has(q, "夜宵", "烧烤", "烤串", "啤酒"))
                score += cat.equals("烧烤") ? 220 : 0;
            if (has(q, "早饭", "早餐", "包子", "馄饨", "饺子", "实惠", "便宜"))
                score += name.contains("沙县") ? 200 : 0;
            if (has(q, "黄焖鸡", "米饭", "盖饭"))
                score += name.contains("黄焖鸡") ? 220 : 0;
            if (has(q, "沙拉", "减脂"))
                score += cat.equals("轻食") ? 200 : 0;

            // 预算意图：预算越低，起送价低的店越占优
            int budget = parseBudget(q);
            if (budget > 0) {
                if (r.minPrice > budget) score -= 150;
                else score += 60 - r.minPrice * 2;
            }

            score += RANDOM.nextDouble() * 20; // 随机扰动，避免每次千篇一律
            if (score > bestScore) {
                bestScore = score;
                best = r;
            }
        }
        if (best != null) return best;
        // 全部休息时取第一家兜底；店铺列表为空则返回 null 由上层处理
        return restaurants.isEmpty() ? null : restaurants.get(0);
    }

    /** 从输入中解析预算数字，如"预算30"、"20元"、"30以内" */
    private static int parseBudget(String q) {
        int n = -1;
        for (int i = 0; i < q.length(); i++) {
            char c = q.charAt(i);
            if (c < '0' || c > '9') continue;
            StringBuilder sb = new StringBuilder();
            while (i < q.length() && q.charAt(i) >= '0' && q.charAt(i) <= '9') {
                sb.append(q.charAt(i++));
            }
            int v = Integer.parseInt(sb.toString());
            if (v >= 5 && v <= 500) n = v;
        }
        return n;
    }

    private static Dish pickDish(DataStore store, Restaurant r) {
        List<Dish> dishes = store.getDishesByRestaurant(r.id);
        return dishes.isEmpty() ? null : dishes.get(RANDOM.nextInt(dishes.size()));
    }

    private static boolean has(String q, String... keys) {
        for (String k : keys) if (q.contains(k)) return true;
        return false;
    }

    private static String reason(Restaurant r, Dish d) {
        return "来自" + r.name + "的" + d.name + "，评分" + r.rating + "、月售" + r.monthlySales
                + "，起送¥" + (int) r.minPrice + "，" + r.deliveryTime + "分钟送达";
    }
}

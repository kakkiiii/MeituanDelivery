package com.example.meituan.data;

import com.example.meituan.model.Address;
import com.example.meituan.model.Coupon;
import com.example.meituan.model.Dish;
import com.example.meituan.model.Order;
import com.example.meituan.model.Restaurant;
import com.example.meituan.model.Review;
import com.example.meituan.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * 内存数据仓库（单例）：模拟后端数据库，三端共享同一份状态。
 */
public class DataStore {

    private static DataStore instance;

    private final List<Restaurant> restaurants;
    private final List<Dish> dishes;
    private final List<Address> addresses;
    private final List<Coupon> coupons;
    private final List<Review> reviews;
    private final List<Order> orders;
    private final List<Order.OrderItem> cart;

    private User currentUser;
    private int orderSeq = 1000;
    private int reviewSeq = 1;
    private int addressSeq = 100;
    private final Random random = new Random();

    // 当前商家归属的店铺 id
    public static final int MERCHANT_RESTAURANT_ID = 1;

    private DataStore() {
        restaurants = MockData.restaurants();
        dishes = MockData.dishes();
        addresses = MockData.addresses();
        coupons = MockData.coupons();
        reviews = new ArrayList<>();
        orders = new ArrayList<>();
        cart = new ArrayList<>();
        seedReviews();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void seedReviews() {
        reviews.add(new Review(reviewSeq++, 0, "蜜雪冰城", 5, "柠檬水超好喝，出餐快！", "10-01 12:30"));
        reviews.add(new Review(reviewSeq++, 0, "肯德基", 4, "味道不错，就是配送稍慢", "10-03 19:20"));
    }

    // ---------- 查询 ----------
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public Restaurant getRestaurant(int id) {
        for (Restaurant r : restaurants) {
            if (r.id == id) return r;
        }
        return null;
    }

    public List<Dish> getDishesByRestaurant(int restaurantId) {
        List<Dish> result = new ArrayList<>();
        for (Dish d : dishes) {
            if (d.restaurantId == restaurantId) result.add(d);
        }
        return result;
    }

    public Dish getDish(int id) {
        for (Dish d : dishes) {
            if (d.id == id) return d;
        }
        return null;
    }

    /**
     * 生成给 AI 的商家/菜品上下文（真实数据，供 DeepSeek 推荐）
     */
    public String aiMenuContext() {
        StringBuilder sb = new StringBuilder();
        for (Restaurant r : restaurants) {
            sb.append("[").append(r.id).append("]").append(r.name)
                    .append("|").append(r.category)
                    .append("|起送").append((long) r.minPrice).append("元\n");
            sb.append("菜品:");
            int n = 0;
            for (Dish d : dishes) {
                if (d.restaurantId != r.id) continue;
                if (n > 0) sb.append(", ");
                sb.append(d.id).append(".").append(d.name)
                        .append("|").append(String.format(Locale.CHINA, "%.1f", d.price));
                n++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<Dish> searchDishes(String keyword) {
        List<Dish> result = new ArrayList<>();
        for (Dish d : dishes) {
            if (d.name.contains(keyword) || d.category.contains(keyword)) result.add(d);
        }
        return result;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public Address getDefaultAddress() {
        for (Address a : addresses) {
            if (a.isDefault) return a;
        }
        return addresses.isEmpty() ? null : addresses.get(0);
    }

    public void addAddress(Address a) {
        a.id = ++addressSeq;
        if (addresses.isEmpty()) a.isDefault = true;
        addresses.add(a);
    }

    public void removeAddress(Address a) {
        addresses.remove(a);
    }

    public void setDefaultAddress(Address a) {
        for (Address x : addresses) x.isDefault = false;
        a.isDefault = true;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public Coupon getCoupon(int id) {
        for (Coupon c : coupons) {
            if (c.id == id) return c;
        }
        return null;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review r) {
        reviews.add(0, r);
    }

    // ---------- 购物车 ----------
    public List<Order.OrderItem> getCart() {
        return cart;
    }

    public void addToCart(Dish dish) {
        for (Order.OrderItem it : cart) {
            if (it.dish.id == dish.id) {
                it.count++;
                return;
            }
        }
        cart.add(new Order.OrderItem(dish, 1));
    }

    public void removeOneFromCart(Dish dish) {
        for (int i = 0; i < cart.size(); i++) {
            Order.OrderItem it = cart.get(i);
            if (it.dish.id == dish.id) {
                if (it.count <= 1) cart.remove(i);
                else it.count--;
                return;
            }
        }
    }

    public void removeFromCart(Dish dish) {
        cart.removeIf(it -> it.dish.id == dish.id);
    }

    public void clearCart() {
        cart.clear();
    }

    public int cartCount() {
        int n = 0;
        for (Order.OrderItem it : cart) n += it.count;
        return n;
    }

    public double cartTotal() {
        double t = 0;
        for (Order.OrderItem it : cart) t += it.subTotal();
        return t;
    }

    public int cartRestaurantId() {
        return cart.isEmpty() ? -1 : cart.get(0).dish.restaurantId;
    }

    // ---------- 订单 ----------
    public List<Order> getOrders() {
        return orders;
    }

    public Order getOrder(int id) {
        for (Order o : orders) {
            if (o.id == id) return o;
        }
        return null;
    }

    public List<Order> getCustomerOrders() {
        List<Order> result = new ArrayList<>(orders);
        result.sort((a, b) -> b.id - a.id);
        return result;
    }

    public List<Order> getMerchantOrders() {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.restaurant.id == MERCHANT_RESTAURANT_ID) result.add(o);
        }
        result.sort((a, b) -> b.id - a.id);
        return result;
    }

    public void placeOrder(Order order) {
        order.id = ++orderSeq;
        order.orderNo = "MT" + System.currentTimeMillis() + (random.nextInt(90) + 10);
        order.status = Order.STATUS_PENDING;
        order.createTime = now();
        orders.add(0, order);
        clearCart();
    }

    public boolean cancelOrder(Order order) {
        if (order.status == Order.STATUS_PENDING) {
            order.status = Order.STATUS_CANCELED;
            return true;
        }
        return false;
    }

    // 商家端操作
    public boolean merchantAccept(Order order) {
        if (order.status == Order.STATUS_PENDING) {
            order.status = Order.STATUS_MERCHANT_ACCEPTED;
            return true;
        }
        return false;
    }

    public boolean merchantReject(Order order) {
        if (order.status == Order.STATUS_PENDING) {
            order.status = Order.STATUS_CANCELED;
            return true;
        }
        return false;
    }

    public boolean merchantReady(Order order) {
        if (order.status == Order.STATUS_MERCHANT_ACCEPTED) {
            order.status = Order.STATUS_READY;
            return true;
        }
        return false;
    }

    // 商家出餐后模拟配送送达（无骑手端）
    public boolean deliverOrder(Order order) {
        if (order.status == Order.STATUS_READY) {
            order.status = Order.STATUS_DELIVERED;
            order.deliverTime = now();
            return true;
        }
        return false;
    }

    // 顾客端评价后完成
    public boolean completeOrder(Order order) {
        if (order.status == Order.STATUS_DELIVERED) {
            order.status = Order.STATUS_COMPLETED;
            return true;
        }
        return false;
    }

    // ---------- 用户 ----------
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public static String now() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}

package com.example.baitapnhom;

import android.app.Application;

import com.example.baitapnhom.data.local.AppDatabase;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.*;

public class ShoppingApplication extends Application {

    private AppDatabase database;
    private PreferencesManager prefs;

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private WishlistRepository wishlistRepository;
    private ReviewRepository reviewRepository;
    private NotificationRepository notificationRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        database = AppDatabase.getInstance(this);
        prefs = new PreferencesManager(this);

        userRepository = new UserRepository(database.userDao());
        productRepository = new ProductRepository(database.productDao());
        categoryRepository = new CategoryRepository(database.categoryDao());
        cartRepository = new CartRepository(database.cartItemDao());
        orderRepository = new OrderRepository(database.orderDao(), database.orderDetailDao());
        wishlistRepository = new WishlistRepository(database.wishlistDao());
        reviewRepository = new ReviewRepository(database.reviewDao());
        notificationRepository = new NotificationRepository(database.notificationDao());
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public PreferencesManager getPrefs() {
        return prefs;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public CartRepository getCartRepository() {
        return cartRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public WishlistRepository getWishlistRepository() {
        return wishlistRepository;
    }

    public ReviewRepository getReviewRepository() {
        return reviewRepository;
    }

    public NotificationRepository getNotificationRepository() {
        return notificationRepository;
    }
}
package com.example.baitapnhom;

import android.app.Application;

import com.example.baitapnhom.data.local.AppDatabase;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.CategoryRepository;
import com.example.baitapnhom.data.repository.OrderRepository;
import com.example.baitapnhom.data.repository.ProductRepository;
import com.example.baitapnhom.data.repository.UserRepository;

public class FruitApplication extends Application {
    private AppDatabase database;
    private PreferencesManager preferencesManager;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDatabase.getInstance(this);
        preferencesManager = new PreferencesManager(this);
        userRepository = new UserRepository(database.userDao());
        categoryRepository = new CategoryRepository(database.categoryDao());
        productRepository = new ProductRepository(database.productDao());
        orderRepository = new OrderRepository(database.orderDao(), database.orderDetailDao(), database.productDao());
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }
}

package com.example.baitapnhom.data.local;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.baitapnhom.data.local.dao.*;
import com.example.baitapnhom.data.local.entity.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                User.class, Category.class, Product.class,
                Order.class, OrderDetail.class, CartItem.class,
                Wishlist.class, Review.class, Notification.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao         userDao();
    public abstract CategoryDao     categoryDao();
    public abstract ProductDao      productDao();
    public abstract OrderDao        orderDao();
    public abstract OrderDetailDao  orderDetailDao();
    public abstract CartItemDao     cartItemDao();
    public abstract WishlistDao     wishlistDao();
    public abstract ReviewDao       reviewDao();
    public abstract NotificationDao notificationDao();

    private static volatile AppDatabase INSTANCE;
    static final ExecutorService dbExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "shopping_db")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    dbExecutor.execute(() -> SeedData.populate(INSTANCE));
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
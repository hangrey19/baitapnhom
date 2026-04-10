package com.example.baitapnhom.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.baitapnhom.data.local.dao.CategoryDao;
import com.example.baitapnhom.data.local.dao.OrderDao;
import com.example.baitapnhom.data.local.dao.OrderDetailDao;
import com.example.baitapnhom.data.local.dao.ProductDao;
import com.example.baitapnhom.data.local.dao.UserDao;
import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.data.local.entity.OrderDetail;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.local.entity.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class},
        version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE products ADD COLUMN expiryDate TEXT NOT NULL DEFAULT '2099-12-31'");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE order_details ADD COLUMN selected INTEGER NOT NULL DEFAULT 1");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE users ADD COLUMN address TEXT");
        }
    };

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "fruit_app_db")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor()
                                            .execute(() -> SeedData.populate(instance));
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }
}
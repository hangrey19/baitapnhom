package com.example.baitapnhom.data.local;

import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.local.entity.User;
import com.example.baitapnhom.utils.DateTimeUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SeedData {
    public static void populate(AppDatabase db) {
        db.userDao().insert(new User("Admin Fruit", "admin", "123456", "0900000001"));
        db.userDao().insert(new User("Nguyen Van A", "user1", "123456", "0900000002"));
        db.userDao().insert(new User("Tran Thi B", "user2", "123456", "0900000003"));

        List<Category> categories = Arrays.asList(
                new Category("Trái cây nội địa", "🍊", "Cam, táo, xoài, ổi tươi mỗi ngày"),
                new Category("Trái cây nhập khẩu", "🍇", "Nho, kiwi, cherry cao cấp"),
                new Category("Nước ép", "🥤", "Nước ép tươi, ít đường, dễ uống"),
                new Category("Combo", "🎁", "Combo tiết kiệm cho văn phòng và gia đình")
        );
        db.categoryDao().insertAll(categories);

        String today = DateTimeUtils.todayIsoDate();

        List<Product> products = Arrays.asList(
                new Product("Tao Envy", "Tao gion ngot, nhap khau, phu hop an truc tiep.", 85000, 50, "kg",
                        "fruit_tao", 2, today, plusDays(11), true),

                new Product("Nho mau don", "Nho mau don hat nho, thom va ngot.", 220000, 20, "hop",
                        "fruit_nho", 2, today, plusDays(14), true),

                new Product("Cam sanh", "Cam mong nuoc, vi chua ngot de uong.", 45000, 60, "kg",
                        "fruit_cam", 1, today, plusDays(5), true),

                new Product("Xoai cat Hoa Loc", "Xoai chin thom, ruot vang, it xo.", 70000, 40, "kg",
                        "fruit_xoai", 1, today, plusDays(8), true),

                new Product("Nuoc ep cam tuoi", "Ep trong ngay, khong duong.", 30000, 30, "chai",
                        "fruit_nuocep", 3, today, plusDays(1), true),

                new Product("Combo trai cay van phong", "Gom tao, nho, kiwi, cam.", 199000, 15, "combo",
                        "fruit_combo", 4, today, plusDays(13), true),

                new Product("Oi le", "Oi gion, it hat, phu hop an vat.", 38000, 35, "kg",
                        "fruit_oi", 1, today, plusDays(6), true),

                new Product("Kiwi vang", "Kiwi vang mem ngot, giau vitamin C.", 125000, 25, "kg",
                        "fruit_kiwi", 2, today, plusDays(12), true),

                new Product("Dua hau cat san", "San pham nay da qua han va se bi an khoi danh sach.", 25000, 10, "hop",
                        "fruit_combo", 1, today, plusDays(-2), false)
        );
        db.productDao().insertAll(products);
    }

    private static String plusDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(calendar.getTime());
    }
}
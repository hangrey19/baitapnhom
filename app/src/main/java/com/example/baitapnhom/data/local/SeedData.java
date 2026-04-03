package com.example.baitapnhom.data.local;

import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.local.entity.User;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SeedData {
    public static void populate(AppDatabase db) {
        db.userDao().insert(new User("Admin Fruit", "admin", "123456", "0900000001"));
        db.userDao().insert(new User("Nguyễn Văn A", "user1", "123456", "0900000002"));
        db.userDao().insert(new User("Trần Thị B", "user2", "123456", "0900000003"));

        List<Category> categories = Arrays.asList(
                new Category("Trái cây nội địa", "🍎", "Cam, táo, xoài, ổi"),
                new Category("Trái cây nhập khẩu", "🍇", "Nho, kiwi, cherry"),
                new Category("Nước ép", "🥤", "Nước ép tươi"),
                new Category("Combo", "🎁", "Combo tiết kiệm")
        );
        db.categoryDao().insertAll(categories);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        List<Product> products = Arrays.asList(
                new Product("Táo Envy", "Táo giòn ngọt, nhập khẩu, phù hợp ăn trực tiếp.", 85000, 50, "kg",
                        "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6", 2, today, true),

                new Product("Nho mẫu đơn", "Nho mẫu đơn hạt nhỏ, thơm và ngọt.", 220000, 20, "hộp",
                        "https://images.unsplash.com/photo-1537640538966-79f369143f8f", 2, today, true),

                new Product("Cam sành", "Cam mọng nước, vị chua ngọt dễ uống.", 45000, 60, "kg",
                        "https://images.unsplash.com/photo-1587735243615-c03f25aaff15", 1, today, true),

                new Product("Xoài cát Hòa Lộc", "Xoài chín thơm, ruột vàng, ít xơ.", 70000, 40, "kg",
                        "https://images.unsplash.com/photo-1553279768-865429fa0078", 1, today, true),

                new Product("Nước ép cam tươi", "Ép trong ngày, không đường.", 30000, 30, "chai",
                        "https://images.unsplash.com/photo-1600271886742-f049cd451bba", 3, today, true),

                new Product("Combo trái cây văn phòng", "Gồm táo, nho, kiwi, cam.", 199000, 15, "combo",
                        "https://images.unsplash.com/photo-1610832958506-aa56368176cf", 4, today, true),

                new Product("Ổi lê", "Ổi giòn, ít hạt, phù hợp ăn vặt.", 38000, 35, "kg",
                        "https://images.unsplash.com/photo-1605027990121-cbae9e0642df", 1, today, true),

                new Product("Kiwi vàng", "Kiwi vàng mềm ngọt, giàu vitamin C.", 125000, 25, "kg",
                        "https://images.unsplash.com/photo-1619566636858-adf3ef46400b", 2, today, true)
        );
        db.productDao().insertAll(products);
    }
}

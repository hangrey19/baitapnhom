package com.example.baitapnhom.data.local;

import com.example.baitapnhom.data.local.entity.*;
import java.util.Arrays;
import java.util.List;

public class SeedData {

    public static void populate(AppDatabase db) {

        // --- Users ---
        User admin = new User("admin", "123456", "admin@fruitshop.com", "0901234567", "123 Lê Lợi, TP.HCM");
        User user1 = new User("user1", "123456", "user1@gmail.com", "0912345678", "456 Nguyễn Huệ, TP.HCM");
        User user2 = new User("user2", "123456", "user2@gmail.com", "0987654321", "789 Trần Hưng Đạo, TP.HCM");

        db.userDao().insert(admin);
        db.userDao().insert(user1);
        db.userDao().insert(user2);

        // --- Categories ---
        List<Category> categories = Arrays.asList(
                new Category("Trái cây nội địa", "🍎", "Trái cây tươi từ Việt Nam"),
                new Category("Trái cây nhập khẩu", "🍓", "Trái cây nhập khẩu cao cấp"),
                new Category("Trái cây theo mùa", "🥭", "Trái cây tươi theo mùa"),
                new Category("Giỏ trái cây", "🧺", "Giỏ trái cây làm quà tặng"),
                new Category("Trái cây cắt sẵn", "🍍", "Trái cây đã gọt và cắt sẵn"),
                new Category("Nước ép trái cây", "🥤", "Nước ép trái cây tươi"),
                new Category("Trái cây sấy", "🍇", "Trái cây sấy khô"),
                new Category("Combo trái cây", "🍑", "Combo nhiều loại trái cây")
        );

        db.categoryDao().insertAll(categories);

        // --- Products ---

        // Trái cây nội địa
        Product p1 = new Product();
        p1.name = "Xoài cát Hòa Lộc";
        p1.price = 85000;
        p1.originalPrice = 100000;
        p1.categoryId = 1;
        p1.rating = 4.8f;
        p1.reviewCount = 230;
        p1.soldCount = 1200;
        p1.isFeatured = true;
        p1.brand = "Việt Nam";
        p1.stock = 100;
        p1.description = "Xoài cát Hòa Lộc ngọt thơm, đặc sản miền Tây.";
        p1.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/9/90/Hoa_Loc_mango.jpg";

        Product p2 = new Product();
        p2.name = "Thanh long ruột đỏ";
        p2.price = 45000;
        p2.originalPrice = 60000;
        p2.categoryId = 1;
        p2.rating = 4.6f;
        p2.reviewCount = 150;
        p2.soldCount = 900;
        p2.isFlashSale = true;
        p2.brand = "Việt Nam";
        p2.stock = 120;
        p2.description = "Thanh long ruột đỏ ngọt thanh, giàu vitamin.";
        p2.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/8/89/Pitaya_cross_section_ed2.jpg";

        // Trái cây nhập khẩu
        Product p3 = new Product();
        p3.name = "Táo Envy New Zealand";
        p3.price = 120000;
        p3.originalPrice = 150000;
        p3.categoryId = 2;
        p3.rating = 4.9f;
        p3.reviewCount = 540;
        p3.soldCount = 2000;
        p3.isFeatured = true;
        p3.brand = "New Zealand";
        p3.stock = 80;
        p3.description = "Táo Envy giòn ngọt, nhập khẩu từ New Zealand.";
        p3.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/15/Red_Apple.jpg";

        Product p4 = new Product();
        p4.name = "Nho đen Mỹ";
        p4.price = 180000;
        p4.originalPrice = 210000;
        p4.categoryId = 2;
        p4.rating = 4.7f;
        p4.reviewCount = 310;
        p4.soldCount = 1100;
        p4.isFlashSale = true;
        p4.brand = "USA";
        p4.stock = 70;
        p4.description = "Nho đen không hạt nhập khẩu từ Mỹ.";
        p4.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/36/Kyoho-grape.jpg";

        // Trái cây theo mùa
        Product p5 = new Product();
        p5.name = "Vải thiều Bắc Giang";
        p5.price = 60000;
        p5.originalPrice = 80000;
        p5.categoryId = 3;
        p5.rating = 4.8f;
        p5.reviewCount = 400;
        p5.soldCount = 1500;
        p5.brand = "Việt Nam";
        p5.stock = 150;
        p5.description = "Vải thiều Bắc Giang ngọt lịm, đặc sản mùa hè.";
        p5.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/2/2f/Lychee_fruit.jpg";

        // Giỏ trái cây
        Product p6 = new Product();
        p6.name = "Giỏ trái cây cao cấp";
        p6.price = 500000;
        p6.originalPrice = 650000;
        p6.categoryId = 4;
        p6.rating = 4.9f;
        p6.reviewCount = 120;
        p6.soldCount = 300;
        p6.isFeatured = true;
        p6.brand = "Fruit Shop";
        p6.stock = 30;
        p6.description = "Giỏ trái cây cao cấp thích hợp làm quà tặng.";
        p6.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/6/6f/Fruit_basket.jpg";

        // Trái cây cắt sẵn
        Product p7 = new Product();
        p7.name = "Dứa cắt sẵn";
        p7.price = 35000;
        p7.originalPrice = 45000;
        p7.categoryId = 5;
        p7.rating = 4.5f;
        p7.reviewCount = 90;
        p7.soldCount = 400;
        p7.brand = "Fruit Shop";
        p7.stock = 60;
        p7.description = "Dứa tươi đã gọt sẵn, tiện lợi.";
        p7.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/cb/Pineapple_and_cross_section.jpg";

        // Nước ép
        Product p8 = new Product();
        p8.name = "Nước ép cam tươi";
        p8.price = 40000;
        p8.originalPrice = 50000;
        p8.categoryId = 6;
        p8.rating = 4.6f;
        p8.reviewCount = 200;
        p8.soldCount = 700;
        p8.brand = "Fruit Shop";
        p8.stock = 80;
        p8.description = "Nước ép cam tươi nguyên chất.";
        p8.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/cd/Orange_juice_1.jpg";

        List<Product> products = Arrays.asList(
                p1,p2,p3,p4,p5,p6,p7,p8
        );

        db.productDao().insertAll(products);

        // --- Reviews ---
        db.reviewDao().insert(new Review(1, 2, 5f, "Xoài rất ngọt và tươi!", "user1"));
        db.reviewDao().insert(new Review(3, 3, 5f, "Táo giòn ngon đúng hàng nhập khẩu.", "user2"));
        db.reviewDao().insert(new Review(5, 2, 4f, "Vải thiều rất thơm.", "user1"));

        // --- Notifications ---
        db.notificationDao().insert(new Notification(1,
                "Chào mừng đến Fruit Shop 🍎",
                "Khám phá trái cây tươi mỗi ngày!",
                Notification.TYPE_GENERAL,
                "🍎"));

        db.notificationDao().insert(new Notification(2,
                "Khuyến mãi trái cây 🍓",
                "Giảm giá đến 30% cho trái cây nhập khẩu hôm nay!",
                Notification.TYPE_PROMOTION,
                "🔥"));
    }
}
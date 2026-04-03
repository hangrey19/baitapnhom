package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "products",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("categoryId")
)
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public double price;
    public double originalPrice;
    public String imageUrl;
    public int categoryId;
    public int stock;
    public float rating;
    public int reviewCount;
    public boolean isFeatured;
    public boolean isFlashSale;
    public int soldCount;
    public String brand;
    public long createdAt;

    public Product() {
        description   = "";
        imageUrl      = "";
        stock         = 100;
        rating        = 0f;
        reviewCount   = 0;
        isFeatured    = false;
        isFlashSale   = false;
        soldCount     = 0;
        brand         = "";
        createdAt     = System.currentTimeMillis();
    }

    public int getDiscountPercent() {
        if (originalPrice > 0 && originalPrice > price) {
            return (int) ((originalPrice - price) / originalPrice * 100);
        }
        return 0;
    }
}
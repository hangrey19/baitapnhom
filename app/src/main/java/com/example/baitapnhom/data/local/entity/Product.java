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
        indices = {@Index("categoryId")}
)
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;
    public double price;
    public int stock;
    public String unit;
    public String imageUrl;
    public int categoryId;
    public String createdDate;
    public String expiryDate;
    public boolean todaySale;

    public Product(String name, String description, double price, int stock,
                   String unit, String imageUrl, int categoryId,
                   String createdDate, String expiryDate, boolean todaySale) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.createdDate = createdDate;
        this.expiryDate = expiryDate;
        this.todaySale = todaySale;
    }
}

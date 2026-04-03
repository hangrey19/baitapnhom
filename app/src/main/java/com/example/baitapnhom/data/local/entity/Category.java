package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String imageUrl;
    public String description;
    public String iconEmoji;

    public Category() {
        imageUrl    = "";
        description = "";
        iconEmoji   = "🛍️";
    }

    public Category(String name, String iconEmoji, String description) {
        this.name        = name;
        this.iconEmoji   = iconEmoji;
        this.description = description;
        this.imageUrl    = "";
    }
}
package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "reviews",
        foreignKeys = {
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,    parentColumns = "id", childColumns = "userId",    onDelete = ForeignKey.CASCADE)
        },
        indices = { @Index("productId"), @Index("userId") }
)
public class Review {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int productId;
    public int userId;
    public float rating;
    public String comment;
    public String username;
    public long createdAt;

    public Review() {
        comment   = "";
        username  = "";
        createdAt = System.currentTimeMillis();
    }

    public Review(int productId, int userId, float rating, String comment, String username) {
        this.productId = productId;
        this.userId    = userId;
        this.rating    = rating;
        this.comment   = comment;
        this.username  = username;
        this.createdAt = System.currentTimeMillis();
    }
}
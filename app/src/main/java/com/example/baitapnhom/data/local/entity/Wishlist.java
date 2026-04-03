package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "wishlist",
        foreignKeys = {
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,    parentColumns = "id", childColumns = "userId",    onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("productId"),
                @Index("userId"),
                @Index(value = {"userId", "productId"}, unique = true)
        }
)
public class Wishlist {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int productId;
    public long addedAt;

    public Wishlist() {
        addedAt = System.currentTimeMillis();
    }

    public Wishlist(int userId, int productId) {
        this.userId    = userId;
        this.productId = productId;
        this.addedAt   = System.currentTimeMillis();
    }
}
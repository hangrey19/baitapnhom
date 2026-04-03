package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "cart_items",
        foreignKeys = {
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,    parentColumns = "id", childColumns = "userId",    onDelete = ForeignKey.CASCADE)
        },
        indices = { @Index("productId"), @Index("userId") }
)
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int productId;
    public int quantity;
    public boolean isSelected;
    public long addedAt;

    public CartItem() {
        quantity   = 1;
        isSelected = true;
        addedAt    = System.currentTimeMillis();
    }

    public CartItem(int userId, int productId, int quantity) {
        this.userId    = userId;
        this.productId = productId;
        this.quantity  = quantity;
        this.isSelected = true;
        this.addedAt   = System.currentTimeMillis();
    }
}
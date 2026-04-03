package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String status;
    public double totalAmount;
    public long createdAt;

    public Order(int userId, String status, double totalAmount, long createdAt) {
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }
}

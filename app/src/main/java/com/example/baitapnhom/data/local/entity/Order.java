package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "orders",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("userId")
)
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public double totalAmount;
    public String status;
    public String address;
    public String paymentMethod;
    public String note;
    public long createdAt;
    public long updatedAt;

    // Status constants
    public static final String STATUS_PENDING   = "PENDING";
    public static final String STATUS_PAID      = "PAID";
    public static final String STATUS_SHIPPING  = "SHIPPING";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    // Payment constants
    public static final String PAY_COD     = "COD";
    public static final String PAY_BANK    = "BANK";
    public static final String PAY_MOMO    = "MOMO";
    public static final String PAY_ZALOPAY = "ZALOPAY";

    public Order() {
        status        = STATUS_PENDING;
        address       = "";
        paymentMethod = PAY_COD;
        note          = "";
        createdAt     = System.currentTimeMillis();
        updatedAt     = System.currentTimeMillis();
    }
}
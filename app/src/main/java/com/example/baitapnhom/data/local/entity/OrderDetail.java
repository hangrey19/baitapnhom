package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "order_details",
        foreignKeys = {
                @ForeignKey(entity = Order.class,   parentColumns = "id", childColumns = "orderId",   onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId", onDelete = ForeignKey.SET_DEFAULT)
        },
        indices = { @Index("orderId"), @Index("productId") }
)
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int orderId;
    public int productId;
    public int quantity;
    public double price;
    public String productName;
    public String productImage;

    public OrderDetail() {
        productImage = "";
    }

    public OrderDetail(int orderId, int productId, int quantity, double price,
                       String productName, String productImage) {
        this.orderId      = orderId;
        this.productId    = productId;
        this.quantity     = quantity;
        this.price        = price;
        this.productName  = productName;
        this.productImage = productImage;
    }
}
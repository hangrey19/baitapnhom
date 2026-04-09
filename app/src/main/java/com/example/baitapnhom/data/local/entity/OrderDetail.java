package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_details")
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int orderId;
    public int productId;
    public int quantity;
    public double unitPrice;
    public boolean selected; // true = được chọn để thanh toán

    public OrderDetail(int orderId, int productId, int quantity, double unitPrice) {
        this.orderId   = orderId;
        this.productId = productId;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
        this.selected  = true; // mặc định chọn khi thêm vào
    }
}
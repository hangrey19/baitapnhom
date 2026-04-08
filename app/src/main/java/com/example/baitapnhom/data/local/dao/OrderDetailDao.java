package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baitapnhom.data.local.entity.OrderDetail;
import com.example.baitapnhom.data.local.model.OrderItemDisplay;

import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    long insert(OrderDetail detail);

    @Update
    void update(OrderDetail detail);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId AND productId = :productId LIMIT 1")
    OrderDetail findByOrderAndProduct(int orderId, int productId);

    @Query("SELECT * FROM order_details WHERE id = :detailId LIMIT 1")
    OrderDetail findById(int detailId);

    @Query("DELETE FROM order_details WHERE id = :detailId")
    void deleteById(int detailId);

    @Query("SELECT COUNT(*) FROM order_details WHERE orderId = :orderId")
    int countByOrderId(int orderId);

    @Query("SELECT od.id AS detailId, od.orderId AS orderId, od.productId AS productId, p.name AS productName, p.imageUrl AS imageUrl, p.unit AS unit, od.quantity AS quantity, od.unitPrice AS unitPrice " +
            "FROM order_details od INNER JOIN products p ON od.productId = p.id WHERE od.orderId = :orderId ORDER BY od.id DESC")
    LiveData<List<OrderItemDisplay>> getDisplayItems(int orderId);

    @Query("SELECT od.id AS detailId, od.orderId AS orderId, od.productId AS productId, p.name AS productName, p.imageUrl AS imageUrl, p.unit AS unit, od.quantity AS quantity, od.unitPrice AS unitPrice " +
            "FROM order_details od INNER JOIN products p ON od.productId = p.id WHERE od.orderId = :orderId ORDER BY od.id DESC")
    List<OrderItemDisplay> getDisplayItemsSync(int orderId);
}

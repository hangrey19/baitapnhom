package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baitapnhom.data.local.entity.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = 'PENDING' LIMIT 1")
    Order getPendingOrder(int userId);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    LiveData<List<Order>> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status != 'PENDING' ORDER BY createdAt DESC")
    LiveData<List<Order>> getPurchaseHistoryByUser(int userId);

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    Order findById(int orderId);

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    LiveData<Order> observeById(int orderId);

    @Query("SELECT COALESCE(SUM(od.quantity), 0) " +
            "FROM orders o " +
            "INNER JOIN order_details od ON o.id = od.orderId " +
            "WHERE o.userId = :userId AND o.status = 'PENDING'")
    LiveData<Integer> getCartItemCount(int userId);

    @Query("SELECT id FROM orders WHERE userId = :userId AND status = 'PAID' ORDER BY createdAt DESC LIMIT 1")
    int getLastPaidOrderId(int userId);
}

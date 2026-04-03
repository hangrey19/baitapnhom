package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    LiveData<List<Order>> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = :status ORDER BY createdAt DESC")
    LiveData<List<Order>> getOrdersByStatus(int userId, String status);

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    LiveData<Order> getOrderById(int id);

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    Order getOrderByIdSync(int id);

    @Query("UPDATE orders SET status = :status, updatedAt = :updatedAt WHERE id = :orderId")
    void updateStatus(int orderId, String status, long updatedAt);

    @Delete
    void delete(Order order);
}
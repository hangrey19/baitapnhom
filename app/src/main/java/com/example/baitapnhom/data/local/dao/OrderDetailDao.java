package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.OrderDetail;
import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<OrderDetail> details);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(OrderDetail detail);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    LiveData<List<OrderDetail>> getDetailsByOrder(int orderId);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getDetailsByOrderSync(int orderId);

    @Query("DELETE FROM order_details WHERE orderId = :orderId")
    void deleteByOrder(int orderId);
}
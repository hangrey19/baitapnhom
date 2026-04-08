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

    // Cập nhật trạng thái selected
    @Query("UPDATE order_details SET selected = :selected WHERE id = :detailId")
    void updateSelected(int detailId, boolean selected);

    // Chọn/bỏ chọn tất cả
    @Query("UPDATE order_details SET selected = :selected WHERE orderId = :orderId")
    void updateAllSelected(int orderId, boolean selected);

    // Query hiển thị — có thêm cột selected
    @Query("SELECT od.id AS detailId, od.orderId AS orderId, od.productId AS productId, " +
            "p.name AS productName, p.imageUrl AS imageUrl, p.unit AS unit, " +
            "od.quantity AS quantity, od.unitPrice AS unitPrice, od.selected AS selected " +
            "FROM order_details od INNER JOIN products p ON od.productId = p.id " +
            "WHERE od.orderId = :orderId ORDER BY od.id DESC")
    LiveData<List<OrderItemDisplay>> getDisplayItems(int orderId);

    @Query("SELECT od.id AS detailId, od.orderId AS orderId, od.productId AS productId, " +
            "p.name AS productName, p.imageUrl AS imageUrl, p.unit AS unit, " +
            "od.quantity AS quantity, od.unitPrice AS unitPrice, od.selected AS selected " +
            "FROM order_details od INNER JOIN products p ON od.productId = p.id " +
            "WHERE od.orderId = :orderId ORDER BY od.id DESC")
    List<OrderItemDisplay> getDisplayItemsSync(int orderId);

    // Chỉ lấy các item được chọn (dùng khi checkout)
    @Query("SELECT od.id AS detailId, od.orderId AS orderId, od.productId AS productId, " +
            "p.name AS productName, p.imageUrl AS imageUrl, p.unit AS unit, " +
            "od.quantity AS quantity, od.unitPrice AS unitPrice, od.selected AS selected " +
            "FROM order_details od INNER JOIN products p ON od.productId = p.id " +
            "WHERE od.orderId = :orderId AND od.selected = 1 ORDER BY od.id DESC")
    List<OrderItemDisplay> getSelectedItemsSync(int orderId);
}
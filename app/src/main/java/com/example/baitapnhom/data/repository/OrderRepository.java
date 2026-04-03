package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;

import com.example.baitapnhom.data.local.dao.OrderDao;
import com.example.baitapnhom.data.local.dao.OrderDetailDao;
import com.example.baitapnhom.data.local.dao.ProductDao;
import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.data.local.entity.OrderDetail;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.data.local.model.OrderItemDisplay;
import com.example.baitapnhom.utils.AppExecutors;

import java.util.List;

public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderDetailDao orderDetailDao;
    private final ProductDao productDao;

    public interface ActionCallback {
        void onDone(boolean success, String message, int orderId);
    }

    public OrderRepository(OrderDao orderDao, OrderDetailDao orderDetailDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderDetailDao = orderDetailDao;
        this.productDao = productDao;
    }

    public void addProductToPendingOrder(int userId, int productId, ActionCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            Product product = productDao.getProductByIdSync(productId);
            if (product == null) {
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "Sản phẩm không tồn tại", -1));
                return;
            }

            Order order = orderDao.getPendingOrder(userId);
            if (order == null) {
                long newId = orderDao.insert(new Order(userId, "PENDING", 0, System.currentTimeMillis()));
                order = orderDao.findById((int) newId);
            }

            OrderDetail detail = orderDetailDao.findByOrderAndProduct(order.id, productId);
            if (detail == null) {
                orderDetailDao.insert(new OrderDetail(order.id, productId, 1, product.price));
            } else {
                detail.quantity = detail.quantity + 1;
                orderDetailDao.update(detail);
            }

            updateOrderTotalInternal(order.id);
            int finalOrderId = order.id;
            AppExecutors.mainThread().execute(() -> callback.onDone(true, "Đã thêm vào hóa đơn tạm", finalOrderId));
        });
    }

    private void updateOrderTotalInternal(int orderId) {
        Order order = orderDao.findById(orderId);
        List<OrderItemDisplay> items = orderDetailDao.getDisplayItemsSync(orderId);
        double total = 0;
        for (OrderItemDisplay item : items) {
            total += item.unitPrice * item.quantity;
        }
        order.totalAmount = total;
        orderDao.update(order);
    }

    public LiveData<Order> observeOrder(int orderId) {
        return orderDao.observeById(orderId);
    }

    public LiveData<List<OrderItemDisplay>> observeOrderItems(int orderId) {
        return orderDetailDao.getDisplayItems(orderId);
    }

    public LiveData<List<Order>> getOrdersByUser(int userId) {
        return orderDao.getOrdersByUser(userId);
    }

    public void checkout(int orderId, ActionCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            Order order = orderDao.findById(orderId);
            if (order == null) {
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "Không tìm thấy hóa đơn", -1));
                return;
            }
            updateOrderTotalInternal(orderId);
            order = orderDao.findById(orderId);
            order.status = "PAID";
            orderDao.update(order);
            AppExecutors.mainThread().execute(() -> callback.onDone(true, "Thanh toán thành công", orderId));
        });
    }

    public void getPendingOrderId(int userId, ActionCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            Order order = orderDao.getPendingOrder(userId);
            int orderId = order != null ? order.id : -1;
            AppExecutors.mainThread().execute(() -> callback.onDone(order != null, order != null ? "OK" : "Chưa có hóa đơn tạm", orderId));
        });
    }
}

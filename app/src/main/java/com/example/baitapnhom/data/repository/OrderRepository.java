package com.example.baitapnhom.data.repository;

import androidx.lifecycle.LiveData;
import com.example.baitapnhom.data.local.dao.OrderDao;
import com.example.baitapnhom.data.local.dao.OrderDetailDao;
import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.data.local.entity.OrderDetail;
import com.example.baitapnhom.utils.AppExecutors;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final OrderDao       orderDao;
    private final OrderDetailDao detailDao;

    public OrderRepository(OrderDao orderDao, OrderDetailDao detailDao) {
        this.orderDao  = orderDao;
        this.detailDao = detailDao;
    }

    public interface OrderCallback { void onCreated(int orderId); }

    public void createOrder(Order order, List<OrderDetail> details, OrderCallback cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            long orderId = orderDao.insert(order);
            List<OrderDetail> withId = new ArrayList<>();
            for (OrderDetail d : details) {
                d.orderId = (int) orderId;
                withId.add(d);
            }
            detailDao.insertAll(withId);
            AppExecutors.getInstance().mainThread().execute(() -> cb.onCreated((int) orderId));
        });
    }

    public LiveData<List<Order>>       getOrdersByUser(int userId)            { return orderDao.getOrdersByUser(userId); }
    public LiveData<List<Order>>       getOrdersByStatus(int userId, String s) { return orderDao.getOrdersByStatus(userId, s); }
    public LiveData<Order>             getOrderById(int id)                   { return orderDao.getOrderById(id); }
    public LiveData<List<OrderDetail>> getDetailsByOrder(int orderId)         { return detailDao.getDetailsByOrder(orderId); }

    public void getDetailsByOrderSync(int orderId, UserRepository.Callback<List<OrderDetail>> cb) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<OrderDetail> list = detailDao.getDetailsByOrderSync(orderId);
            AppExecutors.getInstance().mainThread().execute(() -> cb.onResult(list));
        });
    }

    public void updateStatus(int orderId, String status) {
        AppExecutors.getInstance().diskIO().execute(() ->
                orderDao.updateStatus(orderId, status, System.currentTimeMillis()));
    }

    public void cancelOrder(int orderId) { updateStatus(orderId, Order.STATUS_CANCELLED); }
}
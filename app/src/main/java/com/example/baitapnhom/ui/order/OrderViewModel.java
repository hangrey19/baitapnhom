package com.example.baitapnhom.ui.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.data.local.model.OrderItemDisplay;
import com.example.baitapnhom.data.repository.OrderRepository;

import java.util.List;

public class OrderViewModel extends ViewModel {
    public final MutableLiveData<String> message = new MutableLiveData<>();
    private final OrderRepository repository;

    public OrderViewModel(FruitApplication app) {
        this.repository = app.getOrderRepository();
    }

    public void addProduct(int userId, int productId) {
        repository.addProductToPendingOrder(userId, productId, (success, message, orderId) -> this.message.setValue(message));
    }

    public LiveData<Order> observeOrder(int orderId) {
        return repository.observeOrder(orderId);
    }

    public LiveData<List<OrderItemDisplay>> observeItems(int orderId) {
        return repository.observeOrderItems(orderId);
    }

    public LiveData<List<Order>> getOrdersByUser(int userId) {
        return repository.getOrdersByUser(userId);
    }

    public LiveData<List<Order>> getPurchaseHistoryByUser(int userId) {
        return repository.getPurchaseHistoryByUser(userId);
    }

    public void updateItemQuantity(int detailId, int quantity) {
        repository.updateItemQuantity(detailId, quantity, (success, message, orderId) -> this.message.setValue(message));
    }

    public void removeItem(int detailId) {
        repository.removeItem(detailId, (success, message, orderId) -> this.message.setValue(message));
    }

    public void checkout(int orderId) {
        repository.checkout(orderId, (success, message, id) -> this.message.setValue(message));
    }

    public void getPendingOrderId(int userId, OrderRepository.ActionCallback callback) {
        repository.getPendingOrderId(userId, callback);
    }
}

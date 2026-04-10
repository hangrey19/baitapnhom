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
import com.example.baitapnhom.utils.DateTimeUtils;

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
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "San pham khong ton tai", -1));
                return;
            }
            if (DateTimeUtils.isExpired(product.expiryDate)) {
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "San pham da het han", -1));
                return;
            }
            if (product.stock <= 0) {
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "San pham da het hang", -1));
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
                if (detail.quantity >= product.stock) {
                    int orderId = order.id;
                    AppExecutors.mainThread().execute(() -> callback.onDone(false, "So luong vuot qua ton kho", orderId));
                    return;
                }
                detail.quantity = detail.quantity + 1;
                detail.unitPrice = product.price;
                orderDetailDao.update(detail);
            }

            updateOrderTotalInternal(order.id);
            int finalOrderId = order.id;
            AppExecutors.mainThread().execute(() -> callback.onDone(true, "Da them vao gio hang", finalOrderId));
        });
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

    public LiveData<List<Order>> getPurchaseHistoryByUser(int userId) {
        return orderDao.getPurchaseHistoryByUser(userId);
    }

    public void updateItemQuantity(int detailId, int quantity, ActionCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            OrderDetail detail = orderDetailDao.findById(detailId);
            if (detail == null) {
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "Khong tim thay san pham trong gio", -1));
                return;
            }

            if (quantity <= 0) {
                orderDetailDao.deleteById(detailId);
                updateOrderTotalInternal(detail.orderId);
                AppExecutors.mainThread().execute(() -> callback.onDone(true, "Da xoa san pham khoi gio", detail.orderId));
                return;
            }

            Product product = productDao.getProductByIdSync(detail.productId);
            if (product == null) {
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "San pham khong con ton tai", detail.orderId));
                return;
            }
            if (DateTimeUtils.isExpired(product.expiryDate)) {
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "San pham da het han", detail.orderId));
                return;
            }
            if (quantity > product.stock) {
                AppExecutors.mainThread().execute(() -> callback.onDone(false, "So luong vuot qua ton kho", detail.orderId));
                return;
            }

            detail.quantity = quantity;
            detail.unitPrice = product.price;
            orderDetailDao.update(detail);
            updateOrderTotalInternal(detail.orderId);
            AppExecutors.mainThread().execute(() -> callback.onDone(true, "Da cap nhat gio hang", detail.orderId));
        });
    }

    public void removeItem(int detailId, ActionCallback callback) {
        updateItemQuantity(detailId, 0, callback);
    }



    public void getPendingOrderId(int userId, ActionCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            Order order = orderDao.getPendingOrder(userId);
            int orderId = order != null ? order.id : -1;
            AppExecutors.mainThread().execute(() -> callback.onDone(order != null, order != null ? "OK" : "Chua co gio hang", orderId));
        });
    }
    public LiveData<Integer> getCartItemCount(int userId) {
        return orderDao.getCartItemCount(userId);
    }

    /** Chọn / bỏ chọn 1 item */
    public void setItemSelected(int detailId, boolean selected) {
        AppExecutors.diskIO().execute(() ->
                orderDetailDao.updateSelected(detailId, selected));
    }

    /** Chọn / bỏ chọn tất cả item trong giỏ */
    public void setAllSelected(int orderId, boolean selected) {
        AppExecutors.diskIO().execute(() ->
                orderDetailDao.updateAllSelected(orderId, selected));
    }
    public void getLastPaidOrderId(int userId, java.util.function.Consumer<Integer> callback) {
        AppExecutors.diskIO().execute(() -> {
            int id = orderDao.getLastPaidOrderId(userId);
            AppExecutors.mainThread().execute(() -> callback.accept(id));
        });
    }

    private void updateOrderTotalInternal(int orderId) {
        Order order = orderDao.findById(orderId);
        if (order == null) return;

        List<OrderItemDisplay> items = orderDetailDao.getSelectedItemsSync(orderId);
        double total = 0;
        for (OrderItemDisplay item : items) {
            total += item.unitPrice * item.quantity;
        }
        order.totalAmount = total;
        orderDao.update(order);
    }

    public void checkout(int orderId, ActionCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            Order order = orderDao.findById(orderId);
            if (order == null) {
                AppExecutors.mainThread().execute(() ->
                        callback.onDone(false, "Không tìm thấy giỏ hàng", -1));
                return;
            }

            List<OrderItemDisplay> selectedItems = orderDetailDao.getSelectedItemsSync(orderId);
            if (selectedItems == null || selectedItems.isEmpty()) {
                AppExecutors.mainThread().execute(() ->
                        callback.onDone(false, "Chưa chọn sản phẩm nào để thanh toán", orderId));
                return;
            }

            // Kiểm tra hợp lệ
            for (OrderItemDisplay item : selectedItems) {
                android.util.Log.d("OrderRepo", "Checking item: " + item.productName);
                var product = productDao.getProductByIdSync(item.productId);
                if (product == null) {
                    AppExecutors.mainThread().execute(() ->
                            callback.onDone(false, "Có sản phẩm không còn tồn tại", orderId));
                    return;
                }
                if (com.example.baitapnhom.utils.DateTimeUtils.isExpired(product.expiryDate)) {
                    AppExecutors.mainThread().execute(() ->
                            callback.onDone(false, "Có sản phẩm đã hết hạn trong giỏ", orderId));
                    return;
                }
                if (product.stock < item.quantity) {
                    AppExecutors.mainThread().execute(() ->
                            callback.onDone(false, "Tồn kho không đủ cho " + item.productName, orderId));
                    return;
                }
            }

            // Trừ tồn kho + xóa item đã thanh toán khỏi giỏ
            for (OrderItemDisplay item : selectedItems) {
                var product = productDao.getProductByIdSync(item.productId);
                product.stock -= item.quantity;
                productDao.update(product);
                orderDetailDao.deleteById(item.detailId);
            }

            // Nếu còn item chưa chọn → giỏ vẫn PENDING, chỉ tạo đơn mới PAID
            int remainCount = orderDetailDao.countByOrderId(orderId);

            // Tạo đơn PAID riêng để lưu lịch sử
            double total = 0;
            for (OrderItemDisplay item : selectedItems) total += item.unitPrice * item.quantity;

            Order paidOrder = new Order(order.userId, "PAID", total, System.currentTimeMillis());
            long paidOrderId = orderDao.insert(paidOrder);

            // Nếu giỏ rỗng → đóng đơn PENDING
            if (remainCount == 0) {
                order.status = "PAID";
                order.totalAmount = total;
                orderDao.update(order);
            } else {
                // Cập nhật lại tổng giỏ hàng còn lại
                updateOrderTotalInternal(orderId);
            }

            long finalPaidId = paidOrderId;
            AppExecutors.mainThread().execute(() ->
                    callback.onDone(true, "Thanh toán thành công", (int) finalPaidId));
        });
    }
}

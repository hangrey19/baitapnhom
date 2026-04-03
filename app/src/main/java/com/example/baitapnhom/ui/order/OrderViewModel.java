package com.example.baitapnhom.ui.order;

import androidx.lifecycle.*;
import com.example.baitapnhom.data.local.entity.*;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.*;
import com.example.baitapnhom.ui.cart.CartViewModel.CartItemWithProduct;
import java.util.ArrayList;
import java.util.List;

public class OrderViewModel extends ViewModel {

    public static final int ORDER_IDLE    = 0;
    public static final int ORDER_LOADING = 1;
    public static final int ORDER_SUCCESS = 2;
    public static final int ORDER_ERROR   = 3;

    public static class OrderState {
        public int    state;
        public int    orderId;
        public String errorMsg;

        public static OrderState idle()             { OrderState s = new OrderState(); s.state = ORDER_IDLE;    return s; }
        public static OrderState loading()          { OrderState s = new OrderState(); s.state = ORDER_LOADING; return s; }
        public static OrderState success(int id)    { OrderState s = new OrderState(); s.state = ORDER_SUCCESS; s.orderId = id; return s; }
        public static OrderState error(String msg)  { OrderState s = new OrderState(); s.state = ORDER_ERROR;   s.errorMsg = msg; return s; }
    }

    private final OrderRepository        orderRepo;
    private final CartRepository         cartRepo;
    private final ProductRepository      productRepo;
    private final NotificationRepository notifRepo;
    private final PreferencesManager     prefs;

    private final MutableLiveData<OrderState> _orderState = new MutableLiveData<>(OrderState.idle());
    public  final LiveData<OrderState>         orderState  = _orderState;

    public final LiveData<List<Order>> orders;

    private final MutableLiveData<Integer> _selectedOrderId = new MutableLiveData<>();
    public final LiveData<Order>             selectedOrder;
    public final LiveData<List<OrderDetail>> selectedOrderDetails;

    public OrderViewModel(OrderRepository orderRepo, CartRepository cartRepo,
                          ProductRepository productRepo, NotificationRepository notifRepo,
                          PreferencesManager prefs) {
        this.orderRepo   = orderRepo;
        this.cartRepo    = cartRepo;
        this.productRepo = productRepo;
        this.notifRepo   = notifRepo;
        this.prefs       = prefs;

        selectedOrder = Transformations.switchMap(_selectedOrderId, id -> orderRepo.getOrderById(id));
        selectedOrderDetails = Transformations.switchMap(_selectedOrderId, id -> orderRepo.getDetailsByOrder(id));
        orders = orderRepo.getOrdersByUser(prefs.getLoggedInUserId());
    }

    public void selectOrder(int orderId) { _selectedOrderId.setValue(orderId); }

    public void placeOrder(List<CartItemWithProduct> selectedItems,
                           String address, String paymentMethod, String note) {
        if (address.trim().isEmpty()) {
            _orderState.setValue(OrderState.error("Vui lòng nhập địa chỉ giao hàng")); return;
        }
        _orderState.setValue(OrderState.loading());

        double total = 0;
        for (CartItemWithProduct i : selectedItems) total += i.cartItem.quantity * i.productPrice;

        Order order = new Order();
        order.userId        = prefs.getLoggedInUserId();
        order.totalAmount   = total;
        order.address       = address;
        order.paymentMethod = paymentMethod;
        order.note          = note;
        order.status        = Order.STATUS_PAID;

        List<OrderDetail> details = new ArrayList<>();
        for (CartItemWithProduct item : selectedItems) {
            details.add(new OrderDetail(0, item.cartItem.productId, item.cartItem.quantity,
                    item.productPrice, item.productName, item.productImage));
        }

        double finalTotal = total;
        orderRepo.createOrder(order, details, orderId -> {
            // Update stock & sold counts
            for (CartItemWithProduct item : selectedItems) {
                productRepo.decreaseStock(item.cartItem.productId, item.cartItem.quantity);
                productRepo.increaseSoldCount(item.cartItem.productId, item.cartItem.quantity);
            }
            // Clear cart
            cartRepo.deleteSelectedItems(prefs.getLoggedInUserId());

            // Push notification
            Notification n = new Notification(
                    prefs.getLoggedInUserId(),
                    "Đặt hàng thành công! ✅",
                    "Đơn hàng #" + orderId + " đã được xác nhận. Tổng: " + (long) finalTotal + "đ",
                    Notification.TYPE_ORDER, "✅"
            );
            notifRepo.addNotification(n);

            _orderState.setValue(OrderState.success(orderId));
        });
    }

    public void cancelOrder(int orderId) {
        orderRepo.cancelOrder(orderId);
        Notification n = new Notification(
                prefs.getLoggedInUserId(),
                "Đơn hàng đã hủy ❌",
                "Đơn hàng #" + orderId + " đã được hủy thành công.",
                Notification.TYPE_ORDER, "❌"
        );
        notifRepo.addNotification(n);
    }

    public void resetState() { _orderState.setValue(OrderState.idle()); }
}

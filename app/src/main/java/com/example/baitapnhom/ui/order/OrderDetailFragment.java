package com.example.baitapnhom.ui.order;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.databinding.FragmentOrderDetailBinding;
import com.example.baitapnhom.utils.Constants;
import com.example.baitapnhom.utils.CurrencyFormatter;
import com.example.baitapnhom.utils.DateFormatter;

public class OrderDetailFragment extends Fragment {

    private FragmentOrderDetailBinding b;
    private OrderViewModel vm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentOrderDetailBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();
        vm = new ViewModelProvider(requireActivity(),
                new OrderViewModelFactory(app.getOrderRepository(), app.getCartRepository(),
                        app.getProductRepository(), app.getNotificationRepository(), app.getPrefs())
        ).get(OrderViewModel.class);

        int orderId = getArguments() != null ? getArguments().getInt(Constants.ARG_ORDER_ID, 0) : 0;
        vm.selectOrder(orderId);

        b.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        vm.selectedOrder.observe(getViewLifecycleOwner(), order -> {
            if (order == null) return;
            b.tvOrderId.setText("Đơn hàng #" + order.id);
            b.tvDate.setText(DateFormatter.format(order.createdAt));
            b.tvTotal.setText(CurrencyFormatter.format(order.totalAmount));
            b.tvAddress.setText(order.address);
            b.tvNote.setText(order.note.isEmpty() ? "Không có ghi chú" : order.note);
            b.tvPayment.setText(paymentLabel(order.paymentMethod));
            b.tvStatus.setText(statusLabel(order.status));

            boolean canCancel = order.status.equals(Order.STATUS_PAID) || order.status.equals(Order.STATUS_PENDING);
            b.btnCancel.setVisibility(canCancel ? View.VISIBLE : View.GONE);
            b.btnCancel.setOnClickListener(v -> {
                vm.cancelOrder(order.id);
                Navigation.findNavController(requireView()).navigateUp();
                Toast.makeText(getContext(), "Đã hủy đơn hàng #" + order.id, Toast.LENGTH_SHORT).show();
            });
        });
    }

    private String statusLabel(String status) {
        switch (status) {
            case Order.STATUS_PENDING:   return "⏳ Chờ xử lý";
            case Order.STATUS_PAID:      return "✅ Đã thanh toán";
            case Order.STATUS_SHIPPING:  return "🚚 Đang giao";
            case Order.STATUS_DELIVERED: return "📦 Đã giao";
            case Order.STATUS_CANCELLED: return "❌ Đã hủy";
            default: return status;
        }
    }

    private String paymentLabel(String method) {
        switch (method) {
            case Order.PAY_BANK:    return "🏦 Chuyển khoản";
            case Order.PAY_MOMO:    return "🌸 MoMo";
            case Order.PAY_ZALOPAY: return "🔵 ZaloPay";
            default:                return "💵 COD";
        }
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
package com.example.baitapnhom.ui.order;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.databinding.FragmentPaymentBinding;
import com.example.baitapnhom.utils.CurrencyFormatter;
import com.example.baitapnhom.utils.DateFormatter;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding b;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentPaymentBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();
        OrderViewModel vm = new ViewModelProvider(requireActivity(),
                new OrderViewModelFactory(app.getOrderRepository(), app.getCartRepository(),
                        app.getProductRepository(), app.getNotificationRepository(), app.getPrefs())
        ).get(OrderViewModel.class);

        int orderId = getArguments() != null ? getArguments().getInt("order_id", 0) : 0;
        vm.selectOrder(orderId);

        b.lottieSuccess.playAnimation();

        vm.selectedOrder.observe(getViewLifecycleOwner(), order -> {
            if (order == null) return;
            b.tvOrderId.setText("Đơn hàng #" + order.id);
            b.tvTotal.setText(CurrencyFormatter.format(order.totalAmount));
            b.tvDate.setText(DateFormatter.format(order.createdAt));
            b.tvAddress.setText(order.address);
            b.tvPaymentMethod.setText(paymentLabel(order.paymentMethod));
        });

        b.btnViewOrders.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_paymentFragment_to_orderHistoryFragment));

        b.btnBackHome.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigate(R.id.homeFragment));
    }

    private String paymentLabel(String method) {
        switch (method) {
            case Order.PAY_BANK:    return "🏦 Chuyển khoản ngân hàng";
            case Order.PAY_MOMO:    return "🌸 Ví MoMo";
            case Order.PAY_ZALOPAY: return "🔵 ZaloPay";
            default:                return "💵 Thanh toán khi nhận hàng";
        }
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
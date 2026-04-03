package com.example.baitapnhom.ui.order;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.databinding.ActivityCheckoutBinding;
import com.example.baitapnhom.utils.CurrencyUtils;
import com.example.baitapnhom.utils.ToastUtils;

public class CheckoutActivity extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    private PreferencesManager preferencesManager;
    private OrderViewModel viewModel;
    private int currentOrderId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferencesManager = ((FruitApplication) getApplication()).getPreferencesManager();
        viewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        OrderItemAdapter adapter = new OrderItemAdapter();
        binding.rvItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvItems.setAdapter(adapter);

        if (!preferencesManager.isLoggedIn()) {
            ToastUtils.show(this, "Bạn cần đăng nhập");
            finish();
            return;
        }

        viewModel.getPendingOrderId(preferencesManager.getUserId(), (success, message, orderId) -> {
            if (!success) {
                ToastUtils.show(this, "Chưa có hóa đơn tạm");
                finish();
                return;
            }
            currentOrderId = orderId;
            viewModel.observeItems(orderId).observe(this, adapter::submitList);
            viewModel.observeOrder(orderId).observe(this, order -> {
                if (order != null) {
                    binding.tvTotal.setText(CurrencyUtils.format(order.totalAmount));
                    binding.tvOrderCode.setText("Hóa đơn tạm #" + order.id);
                }
            });
        });

        binding.btnPay.setOnClickListener(v -> {
            if (currentOrderId == -1) return;
            viewModel.checkout(currentOrderId);
        });

        viewModel.message.observe(this, msg -> {
            if (msg != null) {
                ToastUtils.show(this, msg);
                if ("Thanh toán thành công".equals(msg)) {
                    Intent intent = new Intent(this, InvoiceActivity.class);
                    intent.putExtra("order_id", currentOrderId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}

package com.example.baitapnhom.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.model.OrderItemDisplay;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.databinding.ActivityCheckoutBinding;
import com.example.baitapnhom.utils.CurrencyUtils;
import com.example.baitapnhom.utils.SnackbarUtils;

import java.util.Collections;
import java.util.List;

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gio hang");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        preferencesManager = ((FruitApplication) getApplication()).getPreferencesManager();
        viewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        OrderItemAdapter adapter = new OrderItemAdapter(true, new OrderItemAdapter.CartActionListener() {
            @Override
            public void onIncrease(OrderItemDisplay item) {
                viewModel.updateItemQuantity(item.detailId, item.quantity + 1);
            }

            @Override
            public void onDecrease(OrderItemDisplay item) {
                viewModel.updateItemQuantity(item.detailId, item.quantity - 1);
            }

            @Override
            public void onRemove(OrderItemDisplay item) {
                viewModel.removeItem(item.detailId);
            }
        });
        binding.rvItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvItems.setAdapter(adapter);

        if (!preferencesManager.isLoggedIn()) {
            SnackbarUtils.show(this, "Ban can dang nhap");
            finish();
            return;
        }

        loadPendingCart(adapter);

        binding.btnPay.setOnClickListener(v -> {
            if (currentOrderId == -1) {
                SnackbarUtils.show(this, "Gio hang dang trong");
                return;
            }
            viewModel.checkout(currentOrderId);
        });

        viewModel.message.observe(this, msg -> {
            if (msg != null) {
                SnackbarUtils.show(this, msg);
                if ("Thanh toan thanh cong".equals(msg)) {
                    Intent intent = new Intent(this, InvoiceActivity.class);
                    intent.putExtra("order_id", currentOrderId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void loadPendingCart(OrderItemAdapter adapter) {
        viewModel.getPendingOrderId(preferencesManager.getUserId(), (success, message, orderId) -> {
            if (!success) {
                currentOrderId = -1;
                adapter.submitList(Collections.emptyList());
                showCartItems(Collections.emptyList());
                return;
            }

            currentOrderId = orderId;
            viewModel.observeItems(orderId).observe(this, items -> {
                adapter.submitList(items);
                showCartItems(items);
            });
            viewModel.observeOrder(orderId).observe(this, order -> {
                if (order != null) {
                    binding.tvTotal.setText(CurrencyUtils.format(order.totalAmount));
                    binding.tvOrderCode.setText("Gio hang #" + order.id);
                }
            });
        });
    }

    private void showCartItems(List<OrderItemDisplay> items) {
        boolean isEmpty = items == null || items.isEmpty();
        binding.tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.btnPay.setEnabled(!isEmpty);
        if (isEmpty) {
            binding.tvOrderCode.setText("Gio hang cua ban");
            binding.tvTotal.setText(CurrencyUtils.format(0));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

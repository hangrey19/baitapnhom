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
    private PreferencesManager prefs;
    private OrderViewModel viewModel;
    private OrderItemAdapter adapter;
    private int currentOrderId = -1;
    private boolean isUpdatingSelectAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Giỏ hàng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prefs = ((FruitApplication) getApplication()).getPreferencesManager();
        if (!prefs.isLoggedIn()) {
            SnackbarUtils.show(this, "Bạn cần đăng nhập");
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        setupAdapter();
        setupSelectAll();
        loadCart();

        // Bấm "Thanh toán" → mở màn hình xác nhận đơn hàng
        binding.btnPay.setOnClickListener(v -> {
            if (currentOrderId == -1 || adapter.getSelectedCount() == 0) {
                SnackbarUtils.showError(this, "Chưa chọn sản phẩm nào");
                return;
            }
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            intent.putExtra(ConfirmOrderActivity.EXTRA_ORDER_ID, currentOrderId);
            intent.putExtra(ConfirmOrderActivity.EXTRA_TOTAL, adapter.getSelectedTotal());
            startActivity(intent);
        });
    }

    private void setupAdapter() {
        adapter = new OrderItemAdapter(true, new OrderItemAdapter.CartActionListener() {
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

            @Override
            public void onSelectChanged(OrderItemDisplay item, boolean selected) {
                viewModel.setItemSelected(item.detailId, selected);
                updateBottomBar();
                updateSelectAllCheckbox();
            }
        });

        binding.rvItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvItems.setAdapter(adapter);
    }

    private void setupSelectAll() {
        binding.cbSelectAll.setOnCheckedChangeListener((btn, checked) -> {
            if (isUpdatingSelectAll) return;
            if (currentOrderId != -1) {
                viewModel.setAllSelected(currentOrderId, checked);
            }
        });
    }

    private void loadCart() {
        viewModel.getPendingOrderId(prefs.getUserId(), (success, msg, orderId) -> {
            if (!success) {
                currentOrderId = -1;
                showEmptyState();
                return;
            }
            currentOrderId = orderId;
            binding.tvOrderCode.setText("Giỏ hàng #" + orderId);

            viewModel.observeItems(orderId).observe(this, items -> {
                if (items == null || items.isEmpty()) {
                    adapter.submitList(Collections.emptyList());
                    showEmptyState();
                } else {
                    adapter.submitList(items);
                    binding.tvEmpty.setVisibility(View.GONE);
                    binding.cbSelectAll.setVisibility(View.VISIBLE);
                    updateBottomBar();
                    updateSelectAllCheckbox();
                }
            });
        });
    }

    private void updateBottomBar() {
        double total = adapter.getSelectedTotal();
        int count = adapter.getSelectedCount();
        binding.tvTotal.setText(CurrencyUtils.format(total));
        binding.tvSelectedCount.setText("Đã chọn: " + count);
        binding.btnPay.setEnabled(count > 0);
        binding.btnPay.setText("Thanh toán (" + count + ")");
    }

    private void updateSelectAllCheckbox() {
        isUpdatingSelectAll = true;
        binding.cbSelectAll.setChecked(adapter.isAllSelected());
        isUpdatingSelectAll = false;
    }

    private void showEmptyState() {
        binding.tvEmpty.setVisibility(View.VISIBLE);
        binding.cbSelectAll.setVisibility(View.GONE);
        binding.tvSelectedCount.setText("");
        binding.tvTotal.setText(CurrencyUtils.format(0));
        binding.btnPay.setEnabled(false);
        binding.btnPay.setText("Thanh toán");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
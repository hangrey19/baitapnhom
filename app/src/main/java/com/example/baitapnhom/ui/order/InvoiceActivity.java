package com.example.baitapnhom.ui.order;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.databinding.ActivityInvoiceBinding;
import com.example.baitapnhom.utils.CurrencyUtils;
import com.example.baitapnhom.utils.DateTimeUtils;
import com.example.baitapnhom.utils.SnackbarUtils;

public class InvoiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInvoiceBinding binding = ActivityInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi tiet don hang");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int orderId = getIntent().getIntExtra("order_id", -1);
        if (orderId == -1) {
            SnackbarUtils.show(this, "Don hang khong hop le");
            finish();
            return;
        }

        OrderViewModel viewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        OrderItemAdapter adapter = new OrderItemAdapter();
        binding.rvItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvItems.setAdapter(adapter);

        viewModel.observeItems(orderId).observe(this, adapter::submitList);
        viewModel.observeOrder(orderId).observe(this, order -> {
            if (order != null) {
                binding.tvInvoiceCode.setText("Don hang #" + order.id);
                binding.tvStatus.setText("Trang thai: " + order.status);
                binding.tvCreatedAt.setText("Thoi gian mua: " + DateTimeUtils.formatTimestamp(order.createdAt));
                binding.tvTotal.setText(CurrencyUtils.format(order.totalAmount));
            }
        });

        binding.btnDone.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

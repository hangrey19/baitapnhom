package com.example.baitapnhom.ui.order;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.databinding.ActivityInvoiceBinding;
import com.example.baitapnhom.utils.CurrencyUtils;

public class InvoiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInvoiceBinding binding = ActivityInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int orderId = getIntent().getIntExtra("order_id", -1);
        OrderViewModel viewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        OrderItemAdapter adapter = new OrderItemAdapter();
        binding.rvItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvItems.setAdapter(adapter);

        viewModel.observeItems(orderId).observe(this, adapter::submitList);
        viewModel.observeOrder(orderId).observe(this, order -> {
            if (order != null) {
                binding.tvInvoiceCode.setText("Hóa đơn #" + order.id);
                binding.tvStatus.setText("Trạng thái: " + order.status);
                binding.tvTotal.setText(CurrencyUtils.format(order.totalAmount));
            }
        });

        binding.btnDone.setOnClickListener(v -> finish());
    }
}

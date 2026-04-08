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
import com.example.baitapnhom.databinding.ActivityOrderHistoryBinding;
import com.example.baitapnhom.utils.SnackbarUtils;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ActivityOrderHistoryBinding binding;
    private OrderViewModel viewModel;
    private PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lịch sử mua hàng");
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

        OrderHistoryAdapter adapter = new OrderHistoryAdapter(
                // Click vào đơn → xem chi tiết
                order -> {
                    Intent intent = new Intent(this, InvoiceActivity.class);
                    intent.putExtra("order_id", order.id);
                    startActivity(intent);
                },
                // Click mua lại → thêm từng sản phẩm vào giỏ hàng mới
                order -> reorder(order.id)
        );

        binding.rvOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOrders.setAdapter(adapter);

        viewModel.getPurchaseHistoryByUser(prefs.getUserId()).observe(this, orders -> {
            adapter.submitList(orders);
            binding.tvEmpty.setVisibility(
                    orders == null || orders.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * Lấy danh sách sản phẩm của đơn cũ rồi thêm từng cái vào giỏ hàng hiện tại.
     */
    private void reorder(int oldOrderId) {
        int userId = prefs.getUserId();

        // Lấy items của đơn cũ (sync trên background qua ViewModel)
        viewModel.observeItems(oldOrderId).observe(this, items -> {
            if (items == null || items.isEmpty()) {
                SnackbarUtils.showError(this, "Đơn hàng không có sản phẩm");
                return;
            }
            addItemsSequentially(items, 0, userId);
        });
    }

    /**
     * Thêm lần lượt từng sản phẩm vào giỏ (đệ quy để đảm bảo thứ tự).
     */
    private void addItemsSequentially(List<OrderItemDisplay> items, int index, int userId) {
        if (index >= items.size()) {
            // Thêm xong → chuyển thẳng sang giỏ hàng
            SnackbarUtils.showSuccess(this, "✅ Đã thêm " + items.size() + " sản phẩm vào giỏ hàng!");
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }

        OrderItemDisplay item = items.get(index);
        viewModel.addProduct(userId, item.productId);

        viewModel.message.observe(this, msg -> {
            viewModel.message.removeObservers(this);
            addItemsSequentially(items, index + 1, userId);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
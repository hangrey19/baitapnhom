package com.example.baitapnhom.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.User;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.UserRepository;
import com.example.baitapnhom.databinding.ActivityConfirmOrderBinding;
import com.example.baitapnhom.utils.CurrencyUtils;
import com.example.baitapnhom.utils.SnackbarUtils;

public class ConfirmOrderActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "order_id";
    public static final String EXTRA_TOTAL    = "total";

    private ActivityConfirmOrderBinding binding;
    private OrderViewModel orderViewModel;
    private UserRepository userRepository;
    private PreferencesManager prefs;
    private int orderId;
    private double total;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Xác nhận đơn hàng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        orderId = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
        total   = getIntent().getDoubleExtra(EXTRA_TOTAL, 0);

        prefs = ((FruitApplication) getApplication()).getPreferencesManager();
        FruitApplication app = (FruitApplication) getApplication();
        userRepository = app.getUserRepository();

        orderViewModel = new ViewModelProvider(this,
                new OrderViewModelFactory(app)).get(OrderViewModel.class);

        // Hiện tổng tiền
        binding.tvSubtotal.setText(CurrencyUtils.format(total));
        binding.tvTotal.setText(CurrencyUtils.format(total));

        // Load danh sách sản phẩm đã chọn (readonly)
        OrderItemAdapter adapter = new OrderItemAdapter(false, null);
        binding.rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOrderItems.setAdapter(adapter);
        orderViewModel.observeItems(orderId).observe(this, items -> {
            // Chỉ hiện item được selected
            if (items != null) {
                adapter.submitList(items.stream()
                        .filter(i -> i.selected)
                        .collect(java.util.stream.Collectors.toList()));
            }
        });

        // Load thông tin user + địa chỉ
        loadUserInfo();

        // Thay đổi địa chỉ
        binding.tvChangeAddress.setOnClickListener(v -> showAddressInput());
        binding.btnSaveAddress.setOnClickListener(v -> saveAddress());

        // Đặt hàng
        binding.btnPlaceOrder.setOnClickListener(v -> placeOrder());

        // Lắng nghe kết quả checkout
        orderViewModel.message.observe(this, msg -> {
            if (msg == null) return;
            if (msg.contains("thành công")) {
                SnackbarUtils.showSuccess(this, "✅ Đặt hàng thành công!");
                // Mở hóa đơn
                orderViewModel.getLastPaidOrderId(prefs.getUserId(), paidId -> {
                    Intent intent = new Intent(this, InvoiceActivity.class);
                    intent.putExtra("order_id", paidId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                });
            } else {
                SnackbarUtils.showError(this, msg);
            }
        });
    }

    private void loadUserInfo() {
        userRepository.getById(prefs.getUserId(), (user, error) -> {
            if (user == null) return;
            currentUser = user;
            binding.tvReceiverName.setText("👤 " + user.fullName);
            binding.tvReceiverPhone.setText("📞 " + user.phone);

            if (user.address != null && !user.address.isEmpty()) {
                binding.tvAddress.setText("🏠 " + user.address);
                binding.tvAddress.setVisibility(View.VISIBLE);
                binding.tilAddress.setVisibility(View.GONE);
                binding.btnSaveAddress.setVisibility(View.GONE);
            } else {
                binding.tvAddress.setVisibility(View.GONE);
                showAddressInput();
            }
        });
    }

    private void showAddressInput() {
        binding.tilAddress.setVisibility(View.VISIBLE);
        binding.btnSaveAddress.setVisibility(View.VISIBLE);
        if (currentUser != null && currentUser.address != null) {
            binding.etAddress.setText(currentUser.address);
        }
    }

    private void saveAddress() {
        String addr = binding.etAddress.getText() == null ? ""
                : binding.etAddress.getText().toString().trim();

        userRepository.updateAddress(prefs.getUserId(), addr, (success, msg) -> {
            if (success) {
                binding.tvAddress.setText("🏠 " + addr);
                binding.tvAddress.setVisibility(View.VISIBLE);
                binding.tilAddress.setVisibility(View.GONE);
                binding.btnSaveAddress.setVisibility(View.GONE);
                if (currentUser != null) currentUser.address = addr;
                SnackbarUtils.showSuccess(this, "Đã lưu địa chỉ");
            } else {
                SnackbarUtils.showError(this, msg);
            }
        });
    }

    private void placeOrder() {
        // Kiểm tra địa chỉ
        boolean hasAddress = currentUser != null
                && currentUser.address != null
                && !currentUser.address.trim().isEmpty();

        if (!hasAddress) {
            SnackbarUtils.showError(this, "Vui lòng nhập địa chỉ giao hàng");
            showAddressInput();
            return;
        }

        orderViewModel.checkout(orderId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
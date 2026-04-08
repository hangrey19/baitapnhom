package com.example.baitapnhom.ui.product;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.databinding.ActivityProductDetailBinding;
import com.example.baitapnhom.ui.auth.LoginActivity;
import com.example.baitapnhom.ui.order.CheckoutActivity;
import com.example.baitapnhom.ui.order.OrderViewModel;
import com.example.baitapnhom.ui.order.OrderViewModelFactory;
import com.example.baitapnhom.utils.CurrencyUtils;
import com.example.baitapnhom.utils.DateTimeUtils;
import com.example.baitapnhom.utils.ImageLoader;
import com.example.baitapnhom.utils.SnackbarUtils;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private OrderViewModel orderViewModel;
    private PreferencesManager prefs;
    private int productId = -1;
    private boolean canBuy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            SnackbarUtils.showError(this, "Sản phẩm không hợp lệ");
            finish();
            return;
        }

        prefs = ((FruitApplication) getApplication()).getPreferencesManager();

        ProductDetailViewModel viewModel = new ViewModelProvider(this,
                new ProductDetailViewModelFactory((FruitApplication) getApplication()))
                .get(ProductDetailViewModel.class);

        orderViewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        binding.btnBack.setOnClickListener(v -> finish());

        viewModel.load(productId);
        viewModel.getProduct().observe(this, product -> {
            if (product == null) {
                SnackbarUtils.showError(this, "Sản phẩm đã hết hạn hoặc không còn tồn tại");
                finish();
                return;
            }

            binding.tvName.setText(product.name);
            binding.tvDescription.setText(product.description);
            binding.tvPrice.setText(CurrencyUtils.format(product.price));
            binding.tvUnit.setText(product.stock + " " + product.unit);
            binding.tvExpiry.setText(DateTimeUtils.formatIsoDate(product.expiryDate));
            ImageLoader.load(binding.ivProduct, product.imageUrl);

            canBuy = product.stock > 0 && !DateTimeUtils.isExpired(product.expiryDate);
            binding.btnAddToOrder.setEnabled(canBuy);
            binding.btnBuyNow.setEnabled(canBuy);
            if (!canBuy) {
                binding.btnAddToOrder.setText("Hết hàng");
                binding.btnBuyNow.setText("Hết hàng");
            }
        });

        // Thêm vào giỏ hàng
        binding.btnAddToOrder.setOnClickListener(v -> {
            if (!prefs.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            orderViewModel.addProduct(prefs.getUserId(), productId);
        });

        // Mua ngay: thêm vào giỏ rồi chuyển thẳng đến CheckoutActivity
        binding.btnBuyNow.setOnClickListener(v -> {
            if (!prefs.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            orderViewModel.addProduct(prefs.getUserId(), productId);
            // Lắng nghe kết quả rồi chuyển sang checkout
            orderViewModel.message.observe(this, msg -> {
                orderViewModel.message.removeObservers(this);
                if (msg != null) {
                    // Dù thêm mới hay đã có trong giỏ → vẫn mở checkout
                    startActivity(new Intent(this, CheckoutActivity.class));
                }
            });
        });

        // Hiện thông báo Snackbar khi thêm giỏ hàng
        orderViewModel.message.observe(this, msg -> {
            if (msg != null) {
                boolean isSuccess = msg.contains("thêm") || msg.contains("cập nhật");
                if (isSuccess) {
                    SnackbarUtils.showSuccess(binding.getRoot(), "✅ " + msg);
                } else {
                    SnackbarUtils.showError(binding.getRoot(), msg);
                }
            }
        });
    }
}
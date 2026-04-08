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
    private PreferencesManager preferencesManager;
    private int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            SnackbarUtils.show(this, "San pham khong hop le");
            finish();
            return;
        }

        preferencesManager = ((FruitApplication) getApplication()).getPreferencesManager();

        ProductDetailViewModel viewModel = new ViewModelProvider(this,
                new ProductDetailViewModelFactory((FruitApplication) getApplication()))
                .get(ProductDetailViewModel.class);

        orderViewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnViewCart.setOnClickListener(v -> openCartOrLogin());

        viewModel.load(productId);
        viewModel.getProduct().observe(this, product -> {
            if (product == null) {
                SnackbarUtils.show(this, "San pham da het han hoac khong con ton tai");
                finish();
                return;
            }

            binding.tvName.setText(product.name);
            binding.tvDescription.setText(product.description);
            binding.tvPrice.setText(CurrencyUtils.format(product.price));
            binding.tvUnit.setText("Ton kho: " + product.stock + " " + product.unit);
            binding.tvExpiry.setText("Han dung: " + DateTimeUtils.formatIsoDate(product.expiryDate));
            ImageLoader.load(binding.ivProduct, product.imageUrl);

            boolean canAddToCart = product.stock > 0 && !DateTimeUtils.isExpired(product.expiryDate);
            binding.btnAddToOrder.setEnabled(canAddToCart);
            if (!canAddToCart) {
                binding.btnAddToOrder.setText("San pham khong kha dung");
            }
        });

        binding.btnAddToOrder.setOnClickListener(v -> {
            if (!preferencesManager.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            orderViewModel.addProduct(preferencesManager.getUserId(), productId);
        });

        orderViewModel.message.observe(this, msg -> {
            if (msg != null) {
                SnackbarUtils.show(this, msg);
            }
        });
    }

    private void openCartOrLogin() {
        if (!preferencesManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        startActivity(new Intent(this, CheckoutActivity.class));
    }
}

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
import com.example.baitapnhom.utils.ImageLoader;
import com.example.baitapnhom.utils.ToastUtils;

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
        preferencesManager = ((FruitApplication) getApplication()).getPreferencesManager();

        ProductDetailViewModel viewModel = new ViewModelProvider(this,
                new ProductDetailViewModelFactory((FruitApplication) getApplication()))
                .get(ProductDetailViewModel.class);

        orderViewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        viewModel.load(productId);
        viewModel.getProduct().observe(this, product -> {
            if (product == null) return;
            binding.tvName.setText(product.name);
            binding.tvDescription.setText(product.description);
            binding.tvPrice.setText(CurrencyUtils.format(product.price));
            binding.tvStock.setText("Kho: " + product.stock + " " + product.unit);
            ImageLoader.load(binding.ivProduct, product.imageUrl);
        });

        binding.btnAddToOrder.setOnClickListener(v -> {
            if (!preferencesManager.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            orderViewModel.addProduct(preferencesManager.getUserId(), productId);
        });

        binding.btnCheckout.setOnClickListener(v ->
                startActivity(new Intent(this, CheckoutActivity.class)));

        orderViewModel.message.observe(this, msg -> {
            if (msg != null) ToastUtils.show(this, msg);
        });
    }
}
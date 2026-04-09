package com.example.baitapnhom.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.databinding.ActivityProductListByCategoryBinding;

public class ProductListByCategoryActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_ID = "category_id";
    public static final String EXTRA_CATEGORY_NAME = "category_name";

    private ActivityProductListByCategoryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductListByCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, -1);
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoryName == null ? "San pham" : categoryName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ProductViewModel viewModel = new ViewModelProvider(this,
                new ProductViewModelFactory((FruitApplication) getApplication()))
                .get(ProductViewModel.class);

        ProductAdapter adapter = new ProductAdapter(product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            startActivity(intent);
        });

        binding.rcvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rcvProducts.setAdapter(adapter);

        viewModel.loadByCategory(categoryId);
        viewModel.getProducts().observe(this, products -> {
            adapter.submitList(products);
            binding.tvEmpty.setVisibility(products == null || products.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

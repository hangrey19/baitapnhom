package com.example.baitapnhom.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.data.local.model.SearchFilter;
import com.example.baitapnhom.databinding.ActivitySearchBinding;
import com.example.baitapnhom.ui.product.ProductAdapter;
import com.example.baitapnhom.ui.product.ProductDetailActivity;
import com.example.baitapnhom.utils.CurrencyUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private List<Category> categoryList = new ArrayList<>();

    // Để tránh trigger filter khi đang set spinner theo code
    private boolean isSpinnerInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tìm kiếm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this,
                new SearchViewModelFactory((FruitApplication) getApplication()))
                .get(SearchViewModel.class);

        setupRecyclerView();
        setupSearchBox();
        setupSortSpinner();
        setupRangeSlider();
        setupResetButton();
        observeData();

        // Nếu được mở từ HomeFragment với keyword sẵn
        String initKeyword = getIntent().getStringExtra("keyword");
        if (initKeyword != null && !initKeyword.isEmpty()) {
            binding.etSearch.setText(initKeyword);
        }
    }


    private void setupRecyclerView() {
        ProductAdapter adapter = new ProductAdapter(product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            startActivity(intent);
        });
        binding.rvResults.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvResults.setAdapter(adapter);

        viewModel.results.observe(this, products -> {
            adapter.submitList(products);
            boolean empty = products == null || products.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.tvResultCount.setText(empty ? "" :
                    "Tìm thấy " + products.size() + " sản phẩm");
        });
    }

    private void setupSearchBox() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setKeyword(s.toString());
                binding.btnClear.setVisibility(
                        s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        binding.btnClear.setOnClickListener(v -> binding.etSearch.setText(""));
    }

    private void setupSortSpinner() {
        String[] sortLabels = {"Mới nhất", "Giá tăng dần", "Giá giảm dần", "Tên A-Z"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, sortLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSort.setAdapter(adapter);

        binding.spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                SearchFilter.SortOrder order;
                switch (pos) {
                    case 1: order = SearchFilter.SortOrder.PRICE_ASC;  break;
                    case 2: order = SearchFilter.SortOrder.PRICE_DESC; break;
                    case 3: order = SearchFilter.SortOrder.NAME_ASC;   break;
                    default: order = SearchFilter.SortOrder.NEWEST;
                }
                viewModel.setSortOrder(order);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRangeSlider() {
        viewModel.priceRange.observe(this, range -> {
            float min = (float) range[0];
            float max = (float) range[1];
            binding.rangeSlider.setValueFrom(min);
            binding.rangeSlider.setValueTo(max);
            binding.rangeSlider.setValues(min, max);
            updatePriceLabel(min, max);
        });

        binding.rangeSlider.setStepSize(1000f);
        binding.rangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (!fromUser) return;
            List<Float> values = slider.getValues();
            float lo = values.get(0), hi = values.get(1);
            updatePriceLabel(lo, hi);
            viewModel.setPriceRange(lo, hi);
        });
    }

    private void updatePriceLabel(float lo, float hi) {
        binding.tvPriceRange.setText(
                CurrencyUtils.format(lo) + " – " + CurrencyUtils.format(hi));
    }

    private void setupResetButton() {
        binding.btnReset.setOnClickListener(v -> {
            viewModel.resetFilter();
            binding.etSearch.setText("");
            binding.spinnerSort.setSelection(0);
            double[] range = viewModel.priceRange.getValue();
            if (range != null) {
                binding.rangeSlider.setValues((float) range[0], (float) range[1]);
                updatePriceLabel((float) range[0], (float) range[1]);
            }
            binding.chipGroupCategory.check(binding.chipAll.getId());
        });
    }

    private void observeData() {
        viewModel.categories.observe(this, this::buildCategoryChips);
    }

    private void buildCategoryChips(List<Category> categories) {

        categoryList = categories;

        int chipAllId = binding.chipAll.getId();
        binding.chipGroupCategory.removeAllViews();

        binding.chipGroupCategory.addView(binding.chipAll);

        for (Category cat : categories) {
            com.google.android.material.chip.Chip chip =
                    new com.google.android.material.chip.Chip(this);
            chip.setId(cat.id);
            chip.setText(cat.icon + " " + cat.name);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            binding.chipGroupCategory.addView(chip);
        }

        binding.chipGroupCategory.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                group.check(chipAllId);
                return;
            }
            int checkedId = checkedIds.get(0);
            if (checkedId == chipAllId) {
                viewModel.setCategory(0);
            } else {
                viewModel.setCategory(checkedId); // checkedId == categoryId
            }
        });

        binding.chipAll.setChecked(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
package com.example.baitapnhom.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.R;
import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.data.local.model.SearchFilter;
import com.example.baitapnhom.databinding.FragmentHomeBinding;
import com.example.baitapnhom.ui.product.ProductAdapter;
import com.example.baitapnhom.ui.product.ProductDetailActivity;
import com.example.baitapnhom.ui.search.SearchViewModel;
import com.example.baitapnhom.ui.search.SearchViewModelFactory;
import com.example.baitapnhom.utils.CurrencyUtils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SearchViewModel viewModel;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.bind(view);

        viewModel = new ViewModelProvider(this,
                new SearchViewModelFactory((FruitApplication) requireActivity().getApplication()))
                .get(SearchViewModel.class);

        setupRecyclerView();
        setupSearchBox();
        setupSortSpinner();
        setupRangeSlider();
        setupResetButton();

        viewModel.categories.observe(getViewLifecycleOwner(), this::buildCategoryChips);
    }

    // ── RecyclerView

    private void setupRecyclerView() {
        ProductAdapter adapter = new ProductAdapter(product -> {
            Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            startActivity(intent);
        });

        binding.rvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProducts.setAdapter(adapter);

        viewModel.results.observe(getViewLifecycleOwner(), products -> {
            adapter.submitList(products);
            boolean empty = products == null || products.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.tvResultCount.setText(empty ? ""
                    : "Tìm thấy " + products.size() + " sản phẩm");
        });
    }

    // ── Search box
    private void setupSearchBox() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setKeyword(s.toString());
            }
        });
    }

    // ── Sort spinner

    private void setupSortSpinner() {
        String[] labels = {"Mới nhất", "Giá tăng dần", "Giá giảm dần", "Tên A-Z"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSort.setAdapter(adapter);

        binding.spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                SearchFilter.SortOrder order;
                switch (pos) {
                    case 1:  order = SearchFilter.SortOrder.PRICE_ASC;  break;
                    case 2:  order = SearchFilter.SortOrder.PRICE_DESC; break;
                    case 3:  order = SearchFilter.SortOrder.NAME_ASC;   break;
                    default: order = SearchFilter.SortOrder.NEWEST;
                }
                viewModel.setSortOrder(order);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // ── RangeSlider

    private void setupRangeSlider() {
        viewModel.priceRange.observe(getViewLifecycleOwner(), range -> {
            float min = (float) range[0];
            float max = (float) range[1];
            if (min >= max) return;
            binding.rangeSlider.setValueFrom(min);
            binding.rangeSlider.setValueTo(max);
            binding.rangeSlider.setStepSize(1000f);
            binding.rangeSlider.setValues(min, max);
            updatePriceLabel(min, max);
        });

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

    // ── Reset

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

    // ── Category chips

    private void buildCategoryChips(List<Category> categories) {
        binding.chipGroupCategory.removeAllViews();
        binding.chipGroupCategory.addView(binding.chipAll);

        for (Category cat : categories) {
            Chip chip = new Chip(requireContext());
            chip.setId(cat.id);
            chip.setText(cat.icon + " " + cat.name);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            binding.chipGroupCategory.addView(chip);
        }

        int chipAllId = binding.chipAll.getId();
        binding.chipGroupCategory.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                group.check(chipAllId);
                return;
            }
            int checkedId = checkedIds.get(0);
            viewModel.setCategory(checkedId == chipAllId ? 0 : checkedId);
        });

        binding.chipAll.setChecked(true);
    }
}
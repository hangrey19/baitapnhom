package com.example.baitapnhom.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.R;
import com.example.baitapnhom.databinding.FragmentCategoryBinding;
import com.example.baitapnhom.ui.product.ProductListByCategoryActivity;

public class CategoryFragment extends Fragment {
    private FragmentCategoryBinding binding;

    public CategoryFragment() {
        super(R.layout.fragment_category);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.bind(view);

        CategoryViewModel viewModel = new ViewModelProvider(this,
                new CategoryViewModelFactory((FruitApplication) requireActivity().getApplication()))
                .get(CategoryViewModel.class);

        CategoryAdapter adapter = new CategoryAdapter(category -> {
            Intent intent = new Intent(requireContext(), ProductListByCategoryActivity.class);
            intent.putExtra(ProductListByCategoryActivity.EXTRA_CATEGORY_ID, category.id);
            intent.putExtra(ProductListByCategoryActivity.EXTRA_CATEGORY_NAME, category.name);
            startActivity(intent);
        });

        binding.rcvCategories.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rcvCategories.setAdapter(adapter);

        viewModel.getCategories().observe(getViewLifecycleOwner(), adapter::submitList);
    }
}
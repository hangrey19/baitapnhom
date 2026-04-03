package com.example.baitapnhom.ui.home;

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
import com.example.baitapnhom.databinding.FragmentHomeBinding;
import com.example.baitapnhom.ui.product.ProductDetailActivity;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.bind(view);

        HomeViewModel viewModel = new ViewModelProvider(this,
                new HomeViewModelFactory((FruitApplication) requireActivity().getApplication()))
                .get(HomeViewModel.class);

        ProductAdapter adapter = new ProductAdapter(product -> {
            Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            startActivity(intent);
        });

        binding.rvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProducts.setAdapter(adapter);

        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            adapter.submitList(products);
            binding.tvEmpty.setVisibility(products == null || products.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }
}

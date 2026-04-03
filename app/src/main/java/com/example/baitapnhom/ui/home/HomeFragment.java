package com.example.baitapnhom.ui.home;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.FragmentHomeBinding;
import com.example.baitapnhom.ui.category.CategoryAdapter;
import com.example.baitapnhom.ui.product.ProductAdapter;
import com.example.baitapnhom.utils.Constants;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding b;
    private HomeViewModel vm;
    private ProductAdapter  flashSaleAdapter;
    private ProductAdapter  featuredAdapter;
    private CategoryAdapter categoryAdapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup container, Bundle state) {
        b = FragmentHomeBinding.inflate(inf, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();
        vm = new ViewModelProvider(this,
                new HomeViewModelFactory(app.getProductRepository(), app.getCategoryRepository())
        ).get(HomeViewModel.class);

        setupAdapters();
        setupListeners();
        observeData();
    }

    private void setupAdapters() {
        flashSaleAdapter = new ProductAdapter(true,
                product -> navigateToDetail(product.id), null);
        b.rvFlashSale.setAdapter(flashSaleAdapter);
        b.rvFlashSale.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        featuredAdapter = new ProductAdapter(true,
                product -> navigateToDetail(product.id), null);
        b.rvFeatured.setAdapter(featuredAdapter);
        b.rvFeatured.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoryAdapter = new CategoryAdapter(cat -> {
            Bundle args = new Bundle();
            args.putInt(Constants.ARG_CATEGORY_ID, cat.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_homeFragment_to_productListFragment, args);
        });
        b.rvCategories.setAdapter(categoryAdapter);
        b.rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupListeners() {
        b.etSearch.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_productListFragment));

        b.btnCart.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigate(R.id.cartFragment));

        b.btnNotification.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigate(R.id.notificationFragment));

        b.tvSeeAllFlash.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_productListFragment));

        b.tvSeeAllFeatured.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_productListFragment));
    }

    private void observeData() {
        vm.flashSaleProducts.observe(getViewLifecycleOwner(), flashSaleAdapter::submitList);
        vm.featuredProducts.observe(getViewLifecycleOwner(), featuredAdapter::submitList);
        vm.allCategories.observe(getViewLifecycleOwner(), categoryAdapter::submitList);
    }

    private void navigateToDetail(int productId) {
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PRODUCT_ID, productId);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_productDetailFragment, args);
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
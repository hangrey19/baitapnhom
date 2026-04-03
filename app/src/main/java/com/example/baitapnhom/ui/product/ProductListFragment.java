package com.example.baitapnhom.ui.product;

import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import androidx.annotation.*;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.BottomSheetFilterBinding;
import com.example.baitapnhom.databinding.FragmentProductListBinding;
import com.example.baitapnhom.utils.Constants;
import android.widget.Toast;

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding b;
    private ProductViewModel vm;
    private ProductAdapter   adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentProductListBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();
        vm = new ViewModelProvider(requireActivity(),
                new ProductViewModelFactory(app.getProductRepository(), app.getCartRepository(),
                        app.getWishlistRepository(), app.getReviewRepository(), app.getPrefs())
        ).get(ProductViewModel.class);

        // Apply incoming category filter
        if (getArguments() != null) {
            int catId = getArguments().getInt(Constants.ARG_CATEGORY_ID, 0);
            if (catId > 0) {
                vm.filterCategory.setValue(catId);
                vm.applyFilter();
            }
        }

        setupRecyclerView();
        setupSearch();
        setupSort();
        setupFilter();
        observeData();
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(false,
                product -> {
                    Bundle args = new Bundle();
                    args.putInt(Constants.ARG_PRODUCT_ID, product.id);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_productListFragment_to_productDetailFragment, args);
                },
                product -> {
                    vm.addToCart(product.id, 1);
                    Toast.makeText(getContext(), "Đã thêm vào giỏ hàng 🛒", Toast.LENGTH_SHORT).show();
                }
        );
        b.rvProducts.setAdapter(adapter);
        b.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void setupSearch() {
        b.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) {
                vm.searchQuery.setValue(q == null ? "" : q);
                vm.applyFilter(); return true;
            }
            @Override public boolean onQueryTextChange(String q) {
                vm.searchQuery.setValue(q == null ? "" : q);
                vm.applyFilter(); return true;
            }
        });
    }

    private void setupSort() {
        b.chipSort.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            android.widget.ListView lv = new android.widget.ListView(requireContext());
            lv.setAdapter(new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_list_item_1, Constants.SORT_OPTIONS));
            lv.setOnItemClickListener((parent, itemView, pos, id) -> {
                vm.sortBy.setValue(Constants.SORT_KEYS[pos]);
                vm.applyFilter();
                b.chipSort.setText("Sắp xếp: " + Constants.SORT_OPTIONS[pos]);
                dialog.dismiss();
            });
            dialog.setContentView(lv);
            dialog.show();
        });
    }

    private void setupFilter() {
        b.chipFilter.setOnClickListener(v -> {
            BottomSheetFilterBinding sb = BottomSheetFilterBinding.inflate(getLayoutInflater());
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            dialog.setContentView(sb.getRoot());

            sb.btnApply.setOnClickListener(btn -> {
                String minStr = sb.etMinPrice.getText().toString();
                String maxStr = sb.etMaxPrice.getText().toString();
                double minP = minStr.isEmpty() ? 0 : Double.parseDouble(minStr);
                double maxP = maxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxStr);
                vm.filterMinPrice.setValue(minP);
                vm.filterMaxPrice.setValue(maxP);
                vm.filterMinRating.setValue(sb.ratingFilter.getRating());
                vm.applyFilter();
                dialog.dismiss();
            });
            sb.btnReset.setOnClickListener(btn -> {
                vm.resetFilters();
                dialog.dismiss();
            });
            dialog.show();
        });
    }

    private void observeData() {
        vm.filteredProducts.observe(getViewLifecycleOwner(), products -> {
            adapter.submitList(products);
            b.tvEmpty.setVisibility(products == null || products.isEmpty() ? View.VISIBLE : View.GONE);
            b.tvResultCount.setText((products == null ? 0 : products.size()) + " sản phẩm");
        });
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
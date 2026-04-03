package com.example.baitapnhom.ui.product;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.FragmentProductDetailBinding;
import com.example.baitapnhom.ui.auth.LoginActivity;
import com.example.baitapnhom.ui.review.ReviewAdapter;
import com.example.baitapnhom.utils.*;

public class ProductDetailFragment extends Fragment {

    private FragmentProductDetailBinding b;
    private ProductViewModel vm;
    private ReviewAdapter    reviewAdapter;
    private int quantity = 1;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentProductDetailBinding.inflate(inf, c, false);
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

        int productId = getArguments() != null
                ? getArguments().getInt(Constants.ARG_PRODUCT_ID, 0) : 0;
        if (productId == 0) { Navigation.findNavController(view).navigateUp(); return; }
        vm.selectProduct(productId);

        setupReviews();
        setupQuantity();
        observeProduct(app);
        observeWishlist(app);
        observeReviews();

        b.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).navigateUp());
    }

    private void setupReviews() {
        reviewAdapter = new ReviewAdapter();
        b.rvReviews.setAdapter(reviewAdapter);
        b.rvReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        b.rvReviews.setNestedScrollingEnabled(false);
    }

    private void setupQuantity() {
        b.tvQuantity.setText(String.valueOf(quantity));
        b.btnMinus.setOnClickListener(v -> {
            if (quantity > 1) { quantity--; b.tvQuantity.setText(String.valueOf(quantity)); }
        });
        b.btnPlus.setOnClickListener(v -> {
            if (quantity < 99) { quantity++; b.tvQuantity.setText(String.valueOf(quantity)); }
        });
    }

    private void observeProduct(ShoppingApplication app) {
        vm.selectedProduct.observe(getViewLifecycleOwner(), p -> {
            if (p == null) return;

            ImageLoader.load(b.ivProduct, p.imageUrl);
            b.tvName.setText(p.name);
            b.tvBrand.setText(p.brand);
            b.tvPrice.setText(CurrencyFormatter.format(p.price));
            b.tvRating.setText(String.valueOf(p.rating));
            b.tvReviewCount.setText("(" + p.reviewCount + " đánh giá)");
            b.tvSold.setText("Đã bán: " + p.soldCount);
            b.tvStock.setText("Kho: " + p.stock);
            b.ratingBar.setRating(p.rating);
            b.tvDescription.setText(p.description);

            if (p.getDiscountPercent() > 0) {
                b.tvOriginalPrice.setVisibility(View.VISIBLE);
                b.tvOriginalPrice.setText(CurrencyFormatter.format(p.originalPrice));
                // Strikethrough bằng code
                b.tvOriginalPrice.setPaintFlags(
                        b.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                b.tvDiscount.setVisibility(View.VISIBLE);
                b.tvDiscount.setText("-" + p.getDiscountPercent() + "%");
            } else {
                b.tvOriginalPrice.setVisibility(View.GONE);
                b.tvDiscount.setVisibility(View.GONE);
            }

            // Thêm giỏ hàng — yêu cầu đăng nhập
            b.btnAddToCart.setOnClickListener(v -> {
                if (!app.getPrefs().isLoggedIn()) {
                    promptLogin("Vui lòng đăng nhập để thêm vào giỏ hàng"); return;
                }
                vm.addToCart(p.id, quantity);
                Toast.makeText(getContext(),
                        "Đã thêm " + quantity + " sản phẩm vào giỏ 🛒", Toast.LENGTH_SHORT).show();
            });

            // Mua ngay — yêu cầu đăng nhập
            b.btnBuyNow.setOnClickListener(v -> {
                if (!app.getPrefs().isLoggedIn()) {
                    promptLogin("Vui lòng đăng nhập để mua hàng"); return;
                }
                vm.addToCart(p.id, quantity);
                Navigation.findNavController(requireView()).navigate(R.id.cartFragment);
            });

            // Viết đánh giá — yêu cầu đăng nhập
            b.btnWriteReview.setOnClickListener(v -> {
                if (!app.getPrefs().isLoggedIn()) {
                    promptLogin("Vui lòng đăng nhập để viết đánh giá"); return;
                }
                Bundle args = new Bundle();
                args.putInt(Constants.ARG_PRODUCT_ID, p.id);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_productDetailFragment_to_reviewFragment, args);
            });
        });
    }

    private void observeWishlist(ShoppingApplication app) {
        vm.isWishlisted.observe(getViewLifecycleOwner(), wished ->
                b.fabWishlist.setImageResource(wished != null && wished
                        ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline));

        b.fabWishlist.setOnClickListener(v -> {
            if (!app.getPrefs().isLoggedIn()) {
                promptLogin("Vui lòng đăng nhập để lưu yêu thích"); return;
            }
            if (vm.selectedProduct.getValue() != null)
                vm.toggleWishlist(vm.selectedProduct.getValue().id);
        });
    }

    private void observeReviews() {
        vm.productReviews.observe(getViewLifecycleOwner(), reviews -> {
            if (reviews == null) return;
            reviewAdapter.submitList(reviews.size() > 3 ? reviews.subList(0, 3) : reviews);
        });
    }

    /** Hiện toast + chuyển sang LoginActivity */
    private void promptLogin(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
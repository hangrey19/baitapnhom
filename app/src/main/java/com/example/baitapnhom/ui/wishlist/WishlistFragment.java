package com.example.baitapnhom.ui.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.databinding.FragmentWishlistBinding;
import com.example.baitapnhom.ui.auth.LoginActivity;
import com.example.baitapnhom.ui.product.ProductAdapter;
import com.example.baitapnhom.utils.AppExecutors;
import com.example.baitapnhom.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

    private FragmentWishlistBinding b;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentWishlistBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();

        // Chưa đăng nhập → hiện gợi ý, không crash
        if (!app.getPrefs().isLoggedIn()) {
            b.layoutLoginPrompt.setVisibility(View.VISIBLE);
            b.rvWishlist.setVisibility(View.GONE);
            b.tvCount.setVisibility(View.GONE);
            b.tvEmpty.setVisibility(View.GONE);
            b.btnGoLogin.setOnClickListener(v ->
                    startActivity(new Intent(getContext(), LoginActivity.class)));
            return;
        }

        b.layoutLoginPrompt.setVisibility(View.GONE);

        ProductAdapter adapter = new ProductAdapter(false,
                product -> {
                    Bundle args = new Bundle();
                    args.putInt(Constants.ARG_PRODUCT_ID, product.id);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_wishlistFragment_to_productDetailFragment, args);
                }, null
        );

        b.rvWishlist.setAdapter(adapter);
        b.rvWishlist.setLayoutManager(new GridLayoutManager(getContext(), 2));

        int userId = app.getPrefs().getLoggedInUserId();
        app.getWishlistRepository().getWishlistByUser(userId).observe(getViewLifecycleOwner(), wishlist -> {
            if (wishlist == null) { adapter.submitList(new ArrayList<>()); return; }

            AppExecutors.getInstance().diskIO().execute(() -> {
                List<Product> products = new ArrayList<>();
                for (com.example.baitapnhom.data.local.entity.Wishlist w : wishlist) {
                    Product p = app.getDatabase().productDao().getProductByIdSync(w.productId);
                    if (p != null) products.add(p);
                }
                AppExecutors.getInstance().mainThread().execute(() -> {
                    adapter.submitList(products);
                    b.tvCount.setText(products.size() + " sản phẩm yêu thích");
                    b.tvEmpty.setVisibility(products.isEmpty() ? View.VISIBLE : View.GONE);
                });
            });
        });
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
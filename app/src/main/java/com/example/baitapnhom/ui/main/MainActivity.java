package com.example.baitapnhom.ui.main;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.badge.BadgeDrawable;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.ActivityMainBinding;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        NavHostFragment host = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (host == null) return;
        NavController nav = host.getNavController();

        NavigationUI.setupWithNavController(b.bottomNav, nav);

        // Ẩn bottom nav trên các màn hình không có tab
        List<Integer> hideOn = Arrays.asList(
                R.id.productDetailFragment,
                R.id.checkoutFragment,
                R.id.paymentFragment,
                R.id.orderDetailFragment,
                R.id.reviewFragment,
                R.id.notificationFragment,
                R.id.orderHistoryFragment
        );
        nav.addOnDestinationChangedListener((controller, dest, args) ->
                b.bottomNav.setVisibility(
                        hideOn.contains(dest.getId()) ? View.GONE : View.VISIBLE));

        // Badge chỉ trên icon Giỏ hàng — khi đã đăng nhập
        ShoppingApplication app = (ShoppingApplication) getApplication();
        if (app.getPrefs().isLoggedIn()) {
            int userId = app.getPrefs().getLoggedInUserId();

            app.getCartRepository().getCartCount(userId).observe(this, count -> {
                BadgeDrawable badge = b.bottomNav.getOrCreateBadge(R.id.cartFragment);
                if (count != null && count > 0) {
                    badge.setVisible(true);
                    badge.setNumber(count);
                } else {
                    badge.setVisible(false);
                }
            });
        }
    }
}
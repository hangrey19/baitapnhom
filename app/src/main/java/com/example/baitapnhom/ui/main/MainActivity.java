package com.example.baitapnhom.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.R;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.databinding.ActivityMainBinding;
import com.example.baitapnhom.ui.auth.LoginActivity;
import com.example.baitapnhom.ui.category.CategoryFragment;
import com.example.baitapnhom.ui.home.HomeFragment;
import com.example.baitapnhom.ui.order.CheckoutActivity;
import com.example.baitapnhom.ui.order.OrderViewModel;
import com.example.baitapnhom.ui.order.OrderViewModelFactory;
import com.example.baitapnhom.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("🍉 Fruit Shop");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        prefs = ((FruitApplication) getApplication()).getPreferencesManager();

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

        // Điều hướng bottom nav
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (id == R.id.menu_profile) {
                replaceFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        // Icon giỏ hàng
        binding.btnCart.setOnClickListener(v -> {
            if (!prefs.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, CheckoutActivity.class));
            }
        });

        // Observe badge số lượng
        observeCartBadge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh badge mỗi khi quay lại MainActivity
        observeCartBadge();
    }

    private void observeCartBadge() {
        if (!prefs.isLoggedIn()) {
            binding.tvCartBadge.setVisibility(View.GONE);
            return;
        }

        OrderViewModel orderViewModel = new ViewModelProvider(this,
                new OrderViewModelFactory((FruitApplication) getApplication()))
                .get(OrderViewModel.class);

        orderViewModel.getCartItemCount(prefs.getUserId()).observe(this, count -> {
            if (count == null || count == 0) {
                binding.tvCartBadge.setVisibility(View.GONE);
            } else {
                binding.tvCartBadge.setVisibility(View.VISIBLE);
                binding.tvCartBadge.setText(count > 99 ? "99+" : String.valueOf(count));
            }
        });
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
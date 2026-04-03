package com.example.baitapnhom.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.FragmentProfileBinding;
import com.example.baitapnhom.ui.auth.LoginActivity;
import com.example.baitapnhom.utils.AppExecutors;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding b;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentProfileBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();

        if (!app.getPrefs().isLoggedIn()) {
            b.layoutGuest.setVisibility(View.VISIBLE);
            b.layoutLoggedIn.setVisibility(View.GONE);
            b.btnLoginGuest.setOnClickListener(v ->
                    startActivity(new Intent(getContext(), LoginActivity.class)));
            return;
        }

        b.layoutGuest.setVisibility(View.GONE);
        b.layoutLoggedIn.setVisibility(View.VISIBLE);

        // Load user info
        AppExecutors.getInstance().diskIO().execute(() -> {
            com.example.baitapnhom.data.local.entity.User user =
                    app.getDatabase().userDao().getUserByIdSync(app.getPrefs().getLoggedInUserId());
            AppExecutors.getInstance().mainThread().execute(() -> {
                if (user == null || b == null) return;
                b.tvUsername.setText(user.username);
                b.tvEmail.setText(user.email);
                b.tvPhone.setText(user.phone.isEmpty() ? "Chưa cập nhật" : user.phone);
                b.tvAddress.setText(user.address.isEmpty() ? "Chưa cập nhật" : user.address);
            });
        });

        b.btnOrders.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_profileFragment_to_orderHistoryFragment));

        b.btnWishlist.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigate(R.id.wishlistFragment));

        b.switchDarkMode.setChecked(app.getPrefs().isDarkMode());
        b.switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            app.getPrefs().setDarkMode(checked);
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        b.btnLogout.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất?")
                        .setNegativeButton("Hủy", null)
                        .setPositiveButton("Đăng xuất", (dialog, which) -> {
                            app.getPrefs().logout();
                            startActivity(new Intent(getContext(), LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        })
                        .show()
        );
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
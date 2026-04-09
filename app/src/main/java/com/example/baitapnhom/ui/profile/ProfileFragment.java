package com.example.baitapnhom.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.R;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.databinding.FragmentProfileBinding;
import com.example.baitapnhom.ui.auth.LoginActivity;
import com.example.baitapnhom.ui.order.CheckoutActivity;
import com.example.baitapnhom.ui.order.OrderHistoryActivity;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FragmentProfileBinding binding = FragmentProfileBinding.bind(view);
        PreferencesManager prefs = ((FruitApplication) requireActivity().getApplication())
                .getPreferencesManager();

        boolean loggedIn = prefs.isLoggedIn();
        binding.layoutGuest.setVisibility(loggedIn ? View.GONE : View.VISIBLE);
        binding.layoutLoggedIn.setVisibility(loggedIn ? View.VISIBLE : View.GONE);

        binding.btnLogin.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), LoginActivity.class)));

        if (loggedIn) {
            binding.tvUsername.setText(prefs.getUsername());
            binding.cardCart.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), CheckoutActivity.class)));
            binding.cardHistory.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), OrderHistoryActivity.class)));
            binding.btnLogout.setOnClickListener(v -> {
                prefs.logout();
                requireActivity().recreate();
            });
        }
    }
}
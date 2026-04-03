package com.example.baitapnhom.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.databinding.ActivityLoginBinding;
import com.example.baitapnhom.ui.main.MainActivity;
import com.example.baitapnhom.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this,
                new AuthViewModelFactory((FruitApplication) getApplication()))
                .get(AuthViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText() == null ? "" : binding.etUsername.getText().toString();
            String password = binding.etPassword.getText() == null ? "" : binding.etPassword.getText().toString();

            if (username.isBlank() || password.isBlank()) {
                ToastUtils.show(this, "Vui lòng nhập đủ thông tin");
                return;
            }
            viewModel.login(username, password);
        });

        binding.tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        viewModel.message.observe(this, msg -> {
            if (msg != null) ToastUtils.show(this, msg);
        });

        viewModel.success.observe(this, ok -> {
            if (Boolean.TRUE.equals(ok)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }
}

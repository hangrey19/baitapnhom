package com.example.baitapnhom.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.databinding.ActivityLoginBinding;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.ui.main.MainActivity;
import com.example.baitapnhom.utils.SnackbarUtils;

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
            clearErrors();

            String username = text(binding.etUsername);
            String password = text(binding.etPassword);

            if (username.isBlank()) {
                binding.tilUsername.setError("Username bat buoc");
                binding.etUsername.requestFocus();
                return;
            }
            if (password.isBlank()) {
                binding.tilPassword.setError("Mat khau bat buoc");
                binding.etPassword.requestFocus();
                return;
            }
            viewModel.login(username, password);
        });

        binding.tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        viewModel.message.observe(this, msg -> {
            if (msg != null) {
                SnackbarUtils.show(this, msg);
            }
        });

        viewModel.success.observe(this, ok -> {
            if (Boolean.TRUE.equals(ok)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

    private void clearErrors() {
        binding.tilUsername.setError(null);
        binding.tilPassword.setError(null);
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}

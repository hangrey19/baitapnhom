package com.example.baitapnhom.ui.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.databinding.ActivityRegisterBinding;
import com.example.baitapnhom.utils.SnackbarUtils;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this,
                new AuthViewModelFactory((FruitApplication) getApplication()))
                .get(AuthViewModel.class);

        binding.btnRegister.setOnClickListener(v -> {
            clearErrors();

            String fullName = text(binding.etFullName);
            String username = text(binding.etUsername);
            String phone = text(binding.etPhone);
            String password = text(binding.etPassword);
            String confirm = text(binding.etConfirmPassword);

            if (fullName.isBlank()) {
                binding.tilFullName.setError("Ho ten bat buoc");
                binding.etFullName.requestFocus();
                return;
            }
            if (fullName.length() < 3) {
                binding.tilFullName.setError("Ho ten toi thieu 3 ky tu");
                binding.etFullName.requestFocus();
                return;
            }
            if (username.isBlank()) {
                binding.tilUsername.setError("Username bat buoc");
                binding.etUsername.requestFocus();
                return;
            }
            if (username.contains(" ") || username.length() < 4) {
                binding.tilUsername.setError("Username phai tu 4 ky tu va khong co khoang trang");
                binding.etUsername.requestFocus();
                return;
            }
            if (!phone.matches("\\d{10,11}")) {
                binding.tilPhone.setError("So dien thoai phai gom 10-11 chu so");
                binding.etPhone.requestFocus();
                return;
            }
            if (password.length() < 6) {
                binding.tilPassword.setError("Mat khau toi thieu 6 ky tu");
                binding.etPassword.requestFocus();
                return;
            }
            if (!password.equals(confirm)) {
                binding.tilConfirmPassword.setError("Mat khau xac nhan khong khop");
                binding.etConfirmPassword.requestFocus();
                return;
            }

            viewModel.register(fullName, username, password, phone);
        });

        viewModel.message.observe(this, msg -> {
            if (msg != null) {
                SnackbarUtils.show(this, msg);
            }
        });

        viewModel.success.observe(this, ok -> {
            if (Boolean.TRUE.equals(ok)) {
                finish();
            }
        });
    }

    private void clearErrors() {
        binding.tilFullName.setError(null);
        binding.tilUsername.setError(null);
        binding.tilPhone.setError(null);
        binding.tilPassword.setError(null);
        binding.tilConfirmPassword.setError(null);
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}

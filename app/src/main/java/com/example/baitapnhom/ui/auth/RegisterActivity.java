package com.example.baitapnhom.ui.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.databinding.ActivityRegisterBinding;
import com.example.baitapnhom.utils.ToastUtils;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private com.example.baitapnhom.ui.auth.AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this,
                new com.example.baitapnhom.ui.auth.AuthViewModelFactory((FruitApplication) getApplication()))
                .get(com.example.baitapnhom.ui.auth.AuthViewModel.class);

        binding.btnRegister.setOnClickListener(v -> {
            String fullName = text(binding.etFullName);
            String username = text(binding.etUsername);
            String phone = text(binding.etPhone);
            String password = text(binding.etPassword);
            String confirm = text(binding.etConfirmPassword);

            if (fullName.isBlank() || username.isBlank() || phone.isBlank() || password.isBlank()) {
                ToastUtils.show(this, "Vui lòng nhập đủ thông tin");
                return;
            }
            if (!password.equals(confirm)) {
                ToastUtils.show(this, "Mật khẩu xác nhận không khớp");
                return;
            }
            viewModel.register(fullName, username, password, phone);
        });

        viewModel.message.observe(this, msg -> {
            if (msg != null) ToastUtils.show(this, msg);
        });

        viewModel.success.observe(this, ok -> {
            if (Boolean.TRUE.equals(ok)) finish();
        });
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}

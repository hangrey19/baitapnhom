package com.example.baitapnhom.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.ActivityRegisterBinding;
import com.example.baitapnhom.ui.main.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding b;
    private AuthViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        ShoppingApplication app = (ShoppingApplication) getApplication();
        vm = new ViewModelProvider(this,
                new AuthViewModelFactory(app.getUserRepository(), app.getPrefs())
        ).get(AuthViewModel.class);

        b.toolbar.setNavigationOnClickListener(v -> finish());

        vm.authState.observe(this, state -> {
            switch (state.state) {
                case AuthViewModel.STATE_LOADING:
                    b.progressBar.setVisibility(View.VISIBLE);
                    b.btnRegister.setEnabled(false);
                    break;
                case AuthViewModel.STATE_SUCCESS:
                    b.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Đăng ký thành công! 🎉", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finishAffinity();
                    break;
                case AuthViewModel.STATE_ERROR:
                    b.progressBar.setVisibility(View.GONE);
                    b.btnRegister.setEnabled(true);
                    Toast.makeText(this, state.errorMsg, Toast.LENGTH_SHORT).show();
                    vm.resetState();
                    break;
                default:
                    b.progressBar.setVisibility(View.GONE);
                    b.btnRegister.setEnabled(true);
            }
        });

        b.btnRegister.setOnClickListener(v -> vm.register(
                b.etUsername.getText().toString(),
                b.etEmail.getText().toString(),
                b.etPassword.getText().toString(),
                b.etConfirmPassword.getText().toString(),
                b.etPhone.getText().toString()
        ));

        b.tvLogin.setOnClickListener(v -> finish());
    }
}
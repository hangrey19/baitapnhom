package com.example.baitapnhom.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.ActivityLoginBinding;
import com.example.baitapnhom.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;
    private AuthViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        ShoppingApplication app = (ShoppingApplication) getApplication();
        vm = new ViewModelProvider(this,
                new AuthViewModelFactory(app.getUserRepository(), app.getPrefs())
        ).get(AuthViewModel.class);

        vm.authState.observe(this, state -> {
            switch (state.state) {
                case AuthViewModel.STATE_LOADING:
                    b.progressBar.setVisibility(View.VISIBLE);
                    b.btnLogin.setEnabled(false);
                    break;
                case AuthViewModel.STATE_SUCCESS:
                    b.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Chào mừng, " + state.user.username + "! 👋", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finishAffinity();
                    break;
                case AuthViewModel.STATE_ERROR:
                    b.progressBar.setVisibility(View.GONE);
                    b.btnLogin.setEnabled(true);
                    Toast.makeText(this, state.errorMsg, Toast.LENGTH_SHORT).show();
                    vm.resetState();
                    break;
                default:
                    b.progressBar.setVisibility(View.GONE);
                    b.btnLogin.setEnabled(true);
            }
        });

        b.btnLogin.setOnClickListener(v -> vm.login(
                b.etUsername.getText().toString(),
                b.etPassword.getText().toString()
        ));

        b.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        b.tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Liên hệ admin@shop.com để khôi phục", Toast.LENGTH_SHORT).show());
    }
}
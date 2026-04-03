package com.example.baitapnhom.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baitapnhom.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding b = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.ivLogo.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        b.tvAppName.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        b.tvTagline.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));

        // Luôn vào MainActivity — không bắt đăng nhập ngay từ đầu.
        // Người dùng chỉ bị yêu cầu login khi thực hiện hành động cần tài khoản.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 2000);
    }
}
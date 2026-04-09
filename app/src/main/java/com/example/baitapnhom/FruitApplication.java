package com.example.baitapnhom;

import android.app.Application;
import com.example.baitapnhom.data.local.AppDatabase;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.UserRepository;

public class FruitApplication extends Application {

    private AppDatabase database;
    private PreferencesManager preferencesManager;
    private UserRepository userRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo Preferences
        preferencesManager = new PreferencesManager(this);

        // Khởi tạo Repository
        userRepository = new UserRepository();
    }

    // ===== Getter =====
    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
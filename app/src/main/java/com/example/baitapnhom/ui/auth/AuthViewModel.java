package com.example.baitapnhom.ui.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.baitapnhom.FruitApplication;
import com.example.baitapnhom.data.local.entity.User;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.UserRepository;

public class AuthViewModel extends ViewModel {
    public final MutableLiveData<String> message = new MutableLiveData<>();
    public final MutableLiveData<Boolean> success = new MutableLiveData<>(false);

    private final UserRepository userRepository;
    private final PreferencesManager preferencesManager;

    public AuthViewModel(FruitApplication app) {
        this.userRepository = app.getUserRepository();
        this.preferencesManager = app.getPreferencesManager();
    }

    public void login(String username, String password) {
        userRepository.login(username, password, (user, error) -> {
            if (user != null) {
                preferencesManager.saveLogin(user.id, user.username);
                success.setValue(true);
                message.setValue("Đăng nhập thành công");
            } else {
                success.setValue(false);
                message.setValue(error);
            }
        });
    }

    public void register(String fullName, String username, String password, String phone) {
        userRepository.register(fullName, username, password, phone, (ok, msg) -> {
            success.setValue(ok);
            message.setValue(msg);
        });
    }

    public boolean isLoggedIn() {
        return preferencesManager.isLoggedIn();
    }
}

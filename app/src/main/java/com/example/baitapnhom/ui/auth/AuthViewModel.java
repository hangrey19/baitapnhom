package com.example.baitapnhom.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.baitapnhom.data.local.entity.User;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.UserRepository;

public class AuthViewModel extends ViewModel {

    public static final int STATE_IDLE    = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_ERROR   = 3;

    public static class AuthState {
        public int    state;
        public User   user;
        public String errorMsg;

        public static AuthState idle()             { AuthState s = new AuthState(); s.state = STATE_IDLE;    return s; }
        public static AuthState loading()          { AuthState s = new AuthState(); s.state = STATE_LOADING; return s; }
        public static AuthState success(User u)    { AuthState s = new AuthState(); s.state = STATE_SUCCESS; s.user = u; return s; }
        public static AuthState error(String msg)  { AuthState s = new AuthState(); s.state = STATE_ERROR;   s.errorMsg = msg; return s; }
    }

    private final UserRepository     userRepo;
    private final PreferencesManager prefs;
    private final MutableLiveData<AuthState> _state = new MutableLiveData<>(AuthState.idle());
    public  final LiveData<AuthState> authState = _state;

    // Holds newly registered user so Activities can read it
    private User registeredUser;

    public AuthViewModel(UserRepository userRepo, PreferencesManager prefs) {
        this.userRepo = userRepo;
        this.prefs    = prefs;
    }

    public void login(String username, String password) {
        if (username.trim().isEmpty()) { _state.setValue(AuthState.error("Vui lòng nhập tên đăng nhập")); return; }
        if (password.isEmpty())        { _state.setValue(AuthState.error("Vui lòng nhập mật khẩu"));      return; }

        _state.setValue(AuthState.loading());
        userRepo.login(username.trim(), password, user -> {
            if (user != null) {
                prefs.setLoggedIn(true);
                prefs.setLoggedInUserId(user.id);
                prefs.setLoggedInUsername(user.username);
                _state.setValue(AuthState.success(user));
            } else {
                _state.setValue(AuthState.error("Sai tên đăng nhập hoặc mật khẩu"));
            }
        });
    }

    public void register(String username, String email, String password,
                         String confirmPassword, String phone) {
        if (username.trim().length() < 3)  { _state.setValue(AuthState.error("Tên đăng nhập tối thiểu 3 ký tự")); return; }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.setValue(AuthState.error("Email không hợp lệ")); return;
        }
        if (password.length() < 6)         { _state.setValue(AuthState.error("Mật khẩu tối thiểu 6 ký tự")); return; }
        if (!password.equals(confirmPassword)) { _state.setValue(AuthState.error("Mật khẩu xác nhận không khớp")); return; }

        _state.setValue(AuthState.loading());
        User newUser = new User(username.trim(), password, email.trim(), phone.trim(), "");
        userRepo.register(newUser, errorMsg -> {
            if (errorMsg == null) {
                prefs.setLoggedIn(true);
                prefs.setLoggedInUserId(newUser.id);
                prefs.setLoggedInUsername(newUser.username);
                registeredUser = newUser;
                _state.setValue(AuthState.success(newUser));
            } else {
                _state.setValue(AuthState.error(errorMsg));
            }
        });
    }

    public void resetState() { _state.setValue(AuthState.idle()); }
}
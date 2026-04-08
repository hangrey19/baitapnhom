package com.example.baitapnhom.data.repository;

import com.example.baitapnhom.data.local.dao.UserDao;
import com.example.baitapnhom.data.local.entity.User;
import com.example.baitapnhom.utils.AppExecutors;

public class UserRepository {
    private final UserDao userDao;

    public interface UserCallback {
        void onResult(User user, String error);
    }

    public interface SimpleCallback {
        void onComplete(boolean success, String message);
    }

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public void login(String username, String password, UserCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            String u = username == null ? "" : username.trim();
            String p = password == null ? "" : password.trim();
            if (u.isBlank() || p.isBlank()) {
                AppExecutors.mainThread().execute(() ->
                        callback.onResult(null, "Vui lòng nhập đầy đủ thông tin"));
                return;
            }
            User user = userDao.login(u, p);
            AppExecutors.mainThread().execute(() -> {
                if (user != null) callback.onResult(user, null);
                else callback.onResult(null, "Sai tài khoản hoặc mật khẩu");
            });
        });
    }

    public void register(String fullName, String username, String password,
                         String phone, SimpleCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            String fn = fullName  == null ? "" : fullName.trim();
            String u  = username  == null ? "" : username.trim();
            String p  = password  == null ? "" : password.trim();
            String ph = phone     == null ? "" : phone.trim();

            if (fn.length() < 3) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Họ tên không hợp lệ")); return;
            }
            if (u.length() < 4 || u.contains(" ")) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Username không hợp lệ")); return;
            }
            if (!ph.matches("\\d{10,11}")) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Số điện thoại không hợp lệ")); return;
            }
            if (p.length() < 6) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Mật khẩu quá ngắn")); return;
            }
            if (userDao.findByUsername(u) != null) {
                AppExecutors.mainThread().execute(() -> callback.onComplete(false, "Tên đăng nhập đã tồn tại")); return;
            }
            userDao.insert(new User(fn, u, p, ph));
            AppExecutors.mainThread().execute(() -> callback.onComplete(true, "Đăng ký thành công"));
        });
    }

    public void getById(int id, UserCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            User user = userDao.findById(id);
            AppExecutors.mainThread().execute(() ->
                    callback.onResult(user, user == null ? "Không tìm thấy người dùng" : null));
        });
    }

    public void updateAddress(int userId, String address, SimpleCallback callback) {
        AppExecutors.diskIO().execute(() -> {
            String addr = address == null ? "" : address.trim();
            if (addr.isEmpty()) {
                AppExecutors.mainThread().execute(() ->
                        callback.onComplete(false, "Địa chỉ không được để trống"));
                return;
            }
            userDao.updateAddress(userId, addr);
            AppExecutors.mainThread().execute(() ->
                    callback.onComplete(true, "Đã lưu địa chỉ"));
        });
    }
}
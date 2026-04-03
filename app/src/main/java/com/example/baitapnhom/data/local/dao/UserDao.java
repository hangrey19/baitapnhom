package com.example.baitapnhom.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.baitapnhom.data.local.entity.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    LiveData<User> getUserById(int id);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserByIdSync(int id);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int isUsernameTaken(String username);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int isEmailTaken(String email);

    @Delete
    void delete(User user);
}
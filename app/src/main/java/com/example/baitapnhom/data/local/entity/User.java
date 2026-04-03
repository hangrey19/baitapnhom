package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = {"username"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String fullName;
    public String username;
    public String password;
    public String phone;

    public User(String fullName, String username, String password, String phone) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phone = phone;
    }
}

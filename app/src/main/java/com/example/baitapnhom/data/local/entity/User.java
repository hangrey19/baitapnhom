package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String username;
    public String password;
    public String email;
    public String phone;
    public String address;
    public String avatarUrl;
    public long createdAt;

    public User() {
        phone = "";
        address = "";
        avatarUrl = "";
        createdAt = System.currentTimeMillis();
    }

    public User(String username, String password, String email, String phone, String address) {
        this.username  = username;
        this.password  = password;
        this.email     = email;
        this.phone     = phone;
        this.address   = address;
        this.avatarUrl = "";
        this.createdAt = System.currentTimeMillis();
    }
}
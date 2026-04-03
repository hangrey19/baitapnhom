package com.example.baitapnhom.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String title;
    public String message;
    public String type;
    public boolean isRead;
    public String iconEmoji;
    public long createdAt;

    public static final String TYPE_GENERAL   = "GENERAL";
    public static final String TYPE_ORDER     = "ORDER";
    public static final String TYPE_PROMOTION = "PROMOTION";
    public static final String TYPE_SYSTEM    = "SYSTEM";

    public Notification() {
        type      = TYPE_GENERAL;
        isRead    = false;
        iconEmoji = "🔔";
        createdAt = System.currentTimeMillis();
    }

    public Notification(int userId, String title, String message, String type, String iconEmoji) {
        this.userId    = userId;
        this.title     = title;
        this.message   = message;
        this.type      = type;
        this.iconEmoji = iconEmoji;
        this.isRead    = false;
        this.createdAt = System.currentTimeMillis();
    }
}
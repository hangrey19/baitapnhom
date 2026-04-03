package com.example.baitapnhom.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREF_NAME        = "shopping_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID      = "user_id";
    private static final String KEY_USERNAME     = "username";
    private static final String KEY_DARK_MODE    = "dark_mode";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    private static final String KEY_SORT         = "sort_pref";

    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Login state
    public boolean isLoggedIn()            { return prefs.getBoolean(KEY_IS_LOGGED_IN, false); }
    public void    setLoggedIn(boolean v)  { prefs.edit().putBoolean(KEY_IS_LOGGED_IN, v).apply(); }

    // User id
    public int  getLoggedInUserId()        { return prefs.getInt(KEY_USER_ID, -1); }
    public void setLoggedInUserId(int id)  { prefs.edit().putInt(KEY_USER_ID, id).apply(); }

    // Username
    public String getLoggedInUsername()              { return prefs.getString(KEY_USERNAME, ""); }
    public void   setLoggedInUsername(String name)   { prefs.edit().putString(KEY_USERNAME, name).apply(); }

    // Dark mode
    public boolean isDarkMode()            { return prefs.getBoolean(KEY_DARK_MODE, false); }
    public void    setDarkMode(boolean v)  { prefs.edit().putBoolean(KEY_DARK_MODE, v).apply(); }

    // First launch
    public boolean isFirstLaunch()         { return prefs.getBoolean(KEY_FIRST_LAUNCH, true); }
    public void    setFirstLaunch(boolean v){ prefs.edit().putBoolean(KEY_FIRST_LAUNCH, v).apply(); }

    // Sort preference
    public String getSortPreference()            { return prefs.getString(KEY_SORT, "newest"); }
    public void   setSortPreference(String sort) { prefs.edit().putString(KEY_SORT, sort).apply(); }

    public void logout() {
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, false)
                .putInt(KEY_USER_ID, -1)
                .putString(KEY_USERNAME, "")
                .apply();
    }
}
package com.husamali.khatt;

import android.graphics.Bitmap;

public class UserProfile {

    public String username, userUID, dp;

    public UserProfile(String username, String userUID, String dp) {
        this.username = username;
        this.userUID = userUID;
        this.dp = dp;
    }

    public UserProfile() {

    }

    public String getDp() {
        return this.dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUID() {
        return this.userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}

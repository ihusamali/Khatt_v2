package com.husamali.khatt;

import android.graphics.Bitmap;

public class UserProfile {

    public String username, userUID, dp , number;

    public UserProfile(String username, String userUID, String dp , String number) {
        this.username = username;
        this.userUID = userUID;
        this.dp = dp;
        this.number = number;
    }
    public UserProfile(String username, String userUID, String dp) {
        this.username = username;
        this.userUID = userUID;
        this.dp = dp;
        this.number = "";
    }


    public UserProfile() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

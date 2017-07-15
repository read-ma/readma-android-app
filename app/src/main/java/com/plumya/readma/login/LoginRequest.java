package com.plumya.readma.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plumya.readma.data.model.User;

/**
 * Created by miltomasz on 25/06/17.
 */

public class LoginRequest {
    @SerializedName("admin_user")
    @Expose
    public User mUser;

    public LoginRequest(User mUser) {
        this.mUser = mUser;
    }
}

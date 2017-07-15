package com.plumya.readma.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by miltomasz on 25/06/17.
 */

public class User {
    @SerializedName("id")
    @Expose
    public Long id;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("auth_token")
    @Expose
    public String authenticationToken;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

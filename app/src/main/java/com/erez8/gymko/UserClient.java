package com.erez8.gymko;

import android.app.Application;

import com.erez8.gymko.Models.User;


public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

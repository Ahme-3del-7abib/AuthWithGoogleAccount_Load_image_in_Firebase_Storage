package com.example.authappwithgoogleacount.pojo;

import java.io.Serializable;

public class profile implements Serializable {

    private String email, name, img;

    public profile() {
    }

    public profile(String email, String name, String img) {
        this.email = email;
        this.name = name;
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }
}

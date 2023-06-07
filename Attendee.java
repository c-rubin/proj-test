package com.example.studentfx;

import javafx.scene.image.ImageView;

public class Attendee {
    private ImageView image;
    private String username;
    private String name;
    private String lastname;

    public Attendee(ImageView image, String username, String name, String lastname) {
        this.image = image;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}

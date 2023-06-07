package com.example.studentfx;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Friend {
    private ImageView image;
    private String username;
    private String name;
    private String lastname;
    private Button browseBtn;

    public Friend(ImageView image, String username, String name, String lastname) {
        this.image = image;
        this.name = name;
        this.username = username;
        this.lastname = lastname;
        this.browseBtn = createBrowseBtn();
    }

    // Test?
    private Button createBrowseBtn() {
        Button b = new Button("browse");
        b.setStyle("-fx-background-color:  #0f4c75; -fx-text-fill: white; -fx-cursor: hand;");
        b.setOnAction(event -> {
            try {
                CommonActions.goToBrowseMyFriendsPage(event, this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return b;
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

    public Button getBrowseBtn() {
        return browseBtn;
    }

    public void setBrowseBtn(Button browseBtn) {
        this.browseBtn = browseBtn;
    }
}

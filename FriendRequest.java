package com.example.studentfx;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class FriendRequest {
    private ImageView image;
    private String username, name, lastname;
    private Button acceptBtn, rejectBtn;

    public FriendRequest(ImageView image, String username, String name, String lastname) {
        this.image = image;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.acceptBtn = createAcceptBtn();
        this.rejectBtn = createRejectBtn();
    }

    public FriendRequest(ImageView image, String username, String name, String lastname, Button acceptBtn, Button rejectBtn) {
        this.image = image;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.acceptBtn = acceptBtn;
        this.rejectBtn = rejectBtn;
    }


    // Test?
    public Button createAcceptBtn() {
        Button btn = new Button("Accept");
        btn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-cursor: hand;");
        btn.setOnAction(event -> {
            try {
                acceptFriendship();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return btn;
    }

    // Test?
    private Button createRejectBtn() {
        Button btn = new Button("Reject");
        btn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand;");
        btn.setOnAction(event -> {
            try {
                rejectFriendship();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return btn;
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

    public Button getAcceptBtn() {
        return acceptBtn;
    }

    public void setAcceptBtn(Button acceptBtn) {
        this.acceptBtn = acceptBtn;
    }

    public Button getRejectBtn() {
        return rejectBtn;
    }

    public void setRejectBtn(Button rejectBtn) {
        this.rejectBtn = rejectBtn;
    }

    private void acceptFriendship() throws SQLException {
        PreparedStatement pstmt = DB.con.prepareStatement("INSERT INTO shoket (dhenesi, marresi) values (?, ?)");
        pstmt.setString(1, this.username);
        pstmt.setString(2, Data.text);
        pstmt.execute();
        pstmt.close();

        pstmt = DB.con.prepareStatement("INSERT INTO shoket (dhenesi, marresi) values (?, ?)");
        pstmt.setString(1, Data.text);
        pstmt.setString(2, this.username);
        pstmt.execute();
        pstmt.close();

        pstmt = DB.con.prepareStatement("UPDATE friend_requests SET gjendja = 'A'");
        pstmt.executeUpdate();
        pstmt.close();

        Data.reqTable.getItems().remove(this);
    }

    private void rejectFriendship() throws SQLException {
        PreparedStatement pstmt = DB.con.prepareStatement("UPDATE friend_requests SET gjendja = 'R'");
        pstmt.executeUpdate();
        pstmt.close();

        Data.reqTable.getItems().remove(this);
    }
}

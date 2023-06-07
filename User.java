package com.example.studentfx;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private ImageView image;
    private String username;
    private String name;
    private String lastname;
    private Button requestBtn;

    public User(ImageView image, String username, String name, String lastname) {
        this.image = image;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.requestBtn = createRequestBtn();
    }

    // Test?
    private Button createRequestBtn() {
        Button b = new Button("follow");
        b.setStyle("-fx-background-color:  #0f4c75; -fx-text-fill: white; -fx-cursor: hand;");
        b.setOnAction(event -> {
            try {
                sendFriendRequest();
            } catch (SQLException e) {
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

    public Button getRequestBtn() {
        return requestBtn;
    }

    public void setRequestBtn(Button requestBtn) {
        this.requestBtn = requestBtn;
    }

    public void sendFriendRequest() throws SQLException {
        if(!requestExist()) {
            PreparedStatement pstmt = DB.con.prepareStatement("INSERT INTO friend_requests VALUES (?, ?, ?);");
            pstmt.setString(1, Data.text);
            pstmt.setString(2, this.username);
            pstmt.setString(3, "P");
            pstmt.execute();
            pstmt.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Kerkesa u dergua me sukses!");
            alert.showAndWait();
        }
    }

    // Test?
    public boolean requestExist() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT * FROM friend_requests WHERE dhenesi = '"+Data.text+"' AND marresi = '"+this.username+"';";
        ResultSet rs = stmt.executeQuery(query);

        if(rs.next()) {
            if(rs.getString("gjendja").equals("R")) {
                query = "UPDATE friend_requests SET gjendja = 'P' WHERE dhenesi = '"+Data.text+"' AND marresi = '"+this.username+"';";
                stmt.executeUpdate(query);
            }
            return true;
        }
        rs.close();
        return false;
    }
}

package com.example.studentfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentProfileController implements Initializable {

    public Label userLabel, usernameLabel, initials;
    public Button goBackBtn;
    public TextField changeUsernameField, changePasswordField;
    public Button changePictureBtn, backBtn, updateProfileBtn, notificationsBtn;
    public BorderPane mainSectionScene;
    public ImageView profilePic;
    private File file;
    private FileInputStream fis;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        if(Data.notifications) {
           changeNotificationBtn("on", "-fx-background-color:  #2ECC71; -fx-cursor: hand;");
        }
        else {
            changeNotificationBtn("off", "-fx-background-color: red; -fx-cursor: hand;");
        }

        try {
            PreparedStatement pstmt = DB.con.prepareStatement("SELECT stud_foto FROM student WHERE username = '"+ Data.text +"';");
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                Blob b = rs.getBlob("stud_foto");
                if(b != null) {
                    byte[] barr = b.getBytes(1,(int)b.length());

                    FileOutputStream fout = new FileOutputStream("profilePic.jpg");
                    fout.write(barr);
                    fout.close();

                    Image img = new Image("file:profilePic.jpg", 128, 128, true, true);
                    profilePic.setImage(img);
                }
            }

            pstmt.close();

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void changePicture() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(Data.stage);
        if(file != null) {
            Image img = new Image(file.toURI().toString(), 128, 128, true, true);
            profilePic.setImage(img);
            profilePic.setPreserveRatio(true);
        }
    }

    public void checkUsername(KeyEvent event) throws SQLException {
        if(changeUsernameField.getText().equals(Data.text)) {
            changeUsernameField.setStyle("-fx-border-color: red");
        }
        else {
            changeUsernameField.setStyle("-fx-border-color: green");
        }
    }

    public void updateProfile() throws SQLException, FileNotFoundException {
        if(DB.connected()) {
            if(file != null) {
                PreparedStatement pstmt = DB.con.prepareStatement("UPDATE student SET stud_foto = ? WHERE username = '"+Data.text+"';");
                fis = new FileInputStream(file);
                pstmt.setBinaryStream(1, fis, (int) file.length());
                pstmt.executeUpdate();
                pstmt.close();
            }
            if(!changeUsernameField.getText().equals("")) {
                PreparedStatement pstmt = DB.con.prepareStatement("SELECT username FROM student WHERE username <> '"+Data.text+"';");
                ResultSet rs = pstmt.executeQuery();
                boolean update = true;

                while(rs.next()) {
                    if(rs.getString("username").equals(changeUsernameField.getText())) {
                        changeUsernameField.setStyle("-fx-border-color: red");
                        update = false;
                        break;
                    }
                }
                rs.close();
                pstmt.close();

                if(update) {
                    pstmt = DB.con.prepareStatement("UPDATE student SET username = '"+changeUsernameField.getText()+"' WHERE username = '"+Data.text+"';");
                    pstmt.executeUpdate();
                    Data.text = changeUsernameField.getText();
                }
                pstmt.close();
            }
            if(!changePasswordField.getText().equals("")) {
                if(changePasswordField.getText().length() < 30) {
                    PreparedStatement pstmt = DB.con.prepareStatement("UPDATE student SET password = '"+changePasswordField.getText()+"' WHERE username = '"+Data.text+"';");
                    pstmt.executeUpdate();
                    pstmt.close();
                }

            }
        }
    }

    public void changeNotifications(ActionEvent event) throws SQLException {
        Statement stmt = DB.con.createStatement();

        if(Data.notifications) {
            Data.notifications = false;
            changeNotificationBtn("off", "-fx-background-color: red; -fx-cursor: hand;");
        }
        else {
            Data.notifications = true;
            changeNotificationBtn("on", "-fx-background-color:  #2ECC71; -fx-cursor: hand;");
        }
        String query = "UPDATE student SET notification = "+Data.notifications+" WHERE username = '"+Data.text+"';";
        stmt.executeUpdate(query);
        stmt.close();
    }

    public void changeNotificationBtn(String text, String style) {
        notificationsBtn.setText(text);
        notificationsBtn.setStyle(style);
    }

    public void goToCourseListPage(ActionEvent event) throws IOException{
        CommonActions.goToCourseListPage(event, this);
    }

    public void goToMyFriendsPage(ActionEvent event) throws IOException {
        CommonActions.goToMyFriendsPage(event, this);
    }

    public void goToCalendarPage(ActionEvent event) throws IOException {
        CommonActions.goToCalendarPage(event, this);
    }

    public void goToNotificationsPage(ActionEvent event) throws IOException {
        CommonActions.goToNotificationsPage(event, this);
    }

    public void goToAnnouncementsPage(ActionEvent event) throws IOException {
        CommonActions.goToAnnouncementsPage(event, this);
    }

    public void goToStudentProfilePage(ActionEvent event) throws IOException {
        CommonActions.goToStudentProfilePage(event, this);
    }

    public void goBack(ActionEvent event) throws IOException {
        CommonActions.goBack(event, "main-section.fxml", this);
    }

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        CommonActions.goToLoginPage(event, this);
    }
}

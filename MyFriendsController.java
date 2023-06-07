package com.example.studentfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class MyFriendsController implements Initializable {

    public Label userLabel, usernameLabel, initials;
    public TableView<Friend> myFriendsTable;
    public TableColumn<Friend, ImageView> myFriendsImage;
    public TableColumn<Friend, String> myFriendsUsername;
    public TableColumn<Friend, String> myFriendsName;
    public TableColumn<Friend, String> myFriendsLastname;
    public TableColumn<Friend, Button> myFriendsBrowseBtn;

    private ObservableList<Friend> myFriendsTableData = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        try {
            getFriends();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        myFriendsImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        myFriendsUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        myFriendsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        myFriendsLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        myFriendsBrowseBtn.setCellValueFactory(new PropertyValueFactory<>("browseBtn"));

        myFriendsTable.setItems(myFriendsTableData);
    }

    private void getFriends() throws SQLException, IOException {
        if(DB.connected()) {
            Statement stmt = DB.con.createStatement();
            String query = "SELECT * FROM shoket WHERE dhenesi = '"+Data.text+"';";
            ResultSet rs = stmt.executeQuery(query);
            List<String> friends = new ArrayList<>(100);

            while(rs.next()) {
                friends.add(rs.getString("marresi"));
            }

            rs.close();
            stmt.close();

            for(String friend : friends) {
                stmt = DB.con.createStatement();
                query = "SELECT * FROM student WHERE username = '"+friend+"';";
                rs = stmt.executeQuery(query);

                while(rs.next()) {
                    ImageView imageView = null;
                    Blob b = rs.getBlob("stud_foto");
                    if(b != null) {
                        byte[] barr = b.getBytes(1,(int)b.length());

                        FileOutputStream fout = new FileOutputStream("profilePic.jpg");
                        fout.write(barr);
                        fout.close();

                        Image img = new Image("file:profilePic.jpg", 70, 70, true, true);
                        imageView = new ImageView(img);
                        imageView.setFitWidth(70);
                        imageView.setFitWidth(70);
                    }
                    FriendData.username = rs.getString("username");
                    FriendData.major = rs.getString("major");
                    FriendData.minor = rs.getString("minor");

                    myFriendsTableData.add(
                            new Friend(imageView, rs.getString("username"), rs.getString("name"), rs.getString("surname"))
                    );
                }
            }

            rs.close();
            stmt.close();
        }

    }
    @FXML
    private void goBack(ActionEvent event) throws IOException {
        CommonActions.goBack(event, "main-section.fxml", this);
    }

    public void goToCourseListPage(ActionEvent event) throws IOException {
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

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        CommonActions.goToLoginPage(event, this);
    }
}

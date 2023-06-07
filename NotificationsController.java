package com.example.studentfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NotificationsController implements Initializable {

    public Label userLabel, usernameLabel, initials;
    public TableView<FriendRequest> friendRequestTable;
    public TableColumn<FriendRequest, ImageView> pictureFReq;
    public TableColumn<FriendRequest, String> usernameFReq, nameFReq, lastnameFReq;
    public TableColumn<FriendRequest, Button> acceptBtn, rejectBtn;

    public TableView<Course> notificationTabMajor, notificationTabMinor;
    public TableColumn<Course, String> courseMajor, notificationMajor, courseMinor, notificationMinor;
    private ObservableList<FriendRequest> requestData;
    private ObservableList<Course> tabMajorData, tabMinorData;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        Data.reqTable = friendRequestTable;
        requestData = FXCollections.observableArrayList();
        tabMajorData = FXCollections.observableArrayList();
        tabMinorData = FXCollections.observableArrayList();

        try {
            getFriendRequests();
            getCourseNotifications(tabMajorData, "id_kurs_major", "kurset_stud_major", "kurset_major");
            getCourseNotifications(tabMinorData, "id_kurs_minor", "kurset_stud_minor", "kurset_minor");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        pictureFReq.setCellValueFactory(new PropertyValueFactory<>("image"));
        usernameFReq.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameFReq.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastnameFReq.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        acceptBtn.setCellValueFactory(new PropertyValueFactory<>("acceptBtn"));
        rejectBtn.setCellValueFactory(new PropertyValueFactory<>("rejectBtn"));
        friendRequestTable.setItems(requestData);

        courseMajor.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        notificationMajor.setCellValueFactory(new PropertyValueFactory<>("notification"));
        notificationTabMajor.setItems(tabMajorData);

        courseMinor.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        notificationMinor.setCellValueFactory(new PropertyValueFactory<>("notification"));
        notificationTabMinor.setItems(tabMinorData);

    }

    private void getCourseNotifications(ObservableList<Course> data, String id, String tableName1, String tableName2) throws SQLException {
        if(DB.connected()) {
            Statement stmt = DB.con.createStatement();
            String query = "SELECT "+id+" FROM "+tableName1+" WHERE username = '"+Data.text+"' AND lista_saved = true";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                Statement stmt2 = DB.con.createStatement();
                String query2 = "SELECT * FROM "+tableName2+" WHERE "+id+" = '"+rs.getString(id)+"';";
                ResultSet rs2 = stmt2.executeQuery(query2);

                if(rs2.next()) {
                    String[] dateAndTime = getDateAndTime(rs2.getTimestamp("koha_kursit"));
                    int date = Integer.parseInt(dateAndTime[0]);
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    String[] currentDateTime = getDateAndTime(currentTime);
                    int currentDate = Integer.parseInt(currentDateTime[0]);

                    if(currentDate == date - 1 && dateAndTime[1].equals(currentDateTime[1])) {
                        String notification = "Kursi fillon neser ne oren: " + dateAndTime[1];
                        data.add(
                                new Course(rs2.getString(tableName2), notification)
                        );
                    }
                }
                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();
        }
    }

    // Test?
    private void getFriendRequests() throws SQLException, IOException {
        if(DB.connected()) {
            Statement stmt = DB.con.createStatement();
            String query = "SELECT * FROM friend_requests WHERE marresi = '"+Data.text+"' AND gjendja = 'P';";
            ResultSet rs = stmt.executeQuery(query);
            List<String> friends = new ArrayList<>(100);

            while(rs.next()) {
                friends.add(rs.getString("dhenesi"));
            }

            Statement stmt2 = DB.con.createStatement();
            for(String user : friends) {
                String query2 = "SELECT * FROM student WHERE username = '"+user+"';";
                ResultSet rs2 = stmt2.executeQuery(query2);
                ImageView imageView = null;

                if(rs2.next()) {
                    Blob b = rs2.getBlob("stud_foto");
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
                    requestData.add(
                            new FriendRequest(imageView, rs2.getString("username"), rs2.getString("name"), rs2.getString("surname"))
                    );
                }

                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();
        }
    }

    // Test
    private String[] getDateAndTime(Timestamp dateAndTime) {
        String sDT = dateAndTime.toString();
        String[] splited = sDT.split("\\s+");
        String[] splitedDate = splited[0].split("-");
        String[] dateTime = new String[2];
        dateTime[0] = splitedDate[2];
        dateTime[1] = splited[1];
        return dateTime;
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

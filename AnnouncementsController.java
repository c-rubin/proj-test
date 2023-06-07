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

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class AnnouncementsController implements Initializable {

    public Label userLabel, usernameLabel, initials;
    public TableView<Course> ancMajor, ancMinor;
    public TableColumn<Course, String> ancMajorCol, ancMinorCol;
    public TableColumn<Course, Button> ancMajorBtn, ancMinorBtn;
    public ObservableList<Course> ancMajorData = FXCollections.observableArrayList();
    public ObservableList<Course> ancMinorData = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        try {
            getAnnouncements(ancMajorData, "id_kurs_major", "kurset_stud_major", "kurset_major");
            getAnnouncements(ancMinorData, "id_kurs_minor", "kurset_stud_minor", "kurset_minor");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ancMajorCol.setCellValueFactory(new PropertyValueFactory<>("notification"));

        ancMajor.setItems(ancMajorData);

        ancMinorCol.setCellValueFactory(new PropertyValueFactory<>("notification"));

        ancMinor.setItems(ancMinorData);
    }

    private void getAnnouncements(ObservableList<Course> data, String id, String tableName1, String tableName2) throws SQLException {
        if(DB.connected()) {
            Statement stmt = DB.con.createStatement();
            String query = "SELECT "+id+" FROM "+tableName1+" WHERE username = '"+Data.text+"' AND lista_saved = true";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                Statement stmt2 = DB.con.createStatement();
                String query2 = "SELECT * FROM "+tableName2+" WHERE "+id+" = '"+rs.getString(id)+"';";
                ResultSet rs2 = stmt2.executeQuery(query2);

                if(rs2.next()) {
                    String notification = "Kenaqesi qe beheni pjese e kursit "+rs2.getString(tableName2);
                    data.add(
                            new Course(rs2.getString(tableName2), notification)
                    );
                }
                rs2.close();
                stmt2.close();
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

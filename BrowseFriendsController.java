package com.example.studentfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class BrowseFriendsController implements Initializable {
    public Label friendUsername;
    public Label friendMajor;
    public Label friendMinor;
    public Label userLabel, usernameLabel, initials;
    public TableView<Course> majorTabTable, minorTabTable;
    public TableColumn<Course, String> majorTabCourse, minorTabCourse;
    public ObservableList<Course> majorTabTableData, minorTabTableData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        majorTabTableData = FXCollections.observableArrayList();
        minorTabTableData = FXCollections.observableArrayList();

        try {
            getMajorCourses();
            getMinorCourses();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        majorTabCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        majorTabTable.setItems(majorTabTableData);

        minorTabCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        minorTabTable.setItems(minorTabTableData);

        friendUsername.setText(FriendData.username);
        friendMajor.setText(FriendData.major);
        friendMinor.setText(FriendData.minor);
    }

    // Test?
    public void getMajorCourses() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_major FROM kurset_stud_major WHERE username = '"+FriendData.username+"' AND lista_saved = true;";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            Statement stmt2 = DB.con.createStatement();
            String query2 = "SELECT kurset_major FROM kurset_major WHERE id_kurs_major = '"+rs.getString("id_kurs_major")+"';";
            ResultSet rs2 = stmt2.executeQuery(query2);

            if(rs2.next()) {
                majorTabTableData.add(
                        new Course(rs2.getString("kurset_major"))
                );
            }
            rs2.close();
            stmt2.close();
        }
        rs.close();
        stmt.close();
    }

    // Test?
    public void getMinorCourses() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_minor FROM kurset_stud_minor WHERE username = '"+FriendData.username+"' AND lista_saved = true";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            Statement stmt2 = DB.con.createStatement();
            String query2 = "SELECT kurset_minor FROM kurset_minor WHERE id_kurs_minor = '"+rs.getString("id_kurs_minor")+"';";
            ResultSet rs2 = stmt2.executeQuery(query2);

            if(rs2.next()) {
                minorTabTableData.add(
                        new Course(rs2.getString("kurset_minor"))
                );
            }
            rs2.close();
            stmt2.close();
        }
        rs.close();
        stmt.close();
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

    public void goBack(ActionEvent event) throws IOException {
        CommonActions.goBack(event, "my-friends.fxml", this);
    }

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        CommonActions.goToLoginPage(event, this);
    }

}

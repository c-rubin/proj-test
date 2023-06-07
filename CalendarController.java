package com.example.studentfx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class CalendarController implements Initializable {

    public GridPane calendar;
    public Button goBackBtn;
    public Label userLabel, usernameLabel, initials;
    public ObservableList<Node> days;

    public void goBack(ActionEvent event) throws IOException {
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

    public void goToAnnouncementsPage(ActionEvent event) {
    }

    public void goToStudentProfilePage(ActionEvent event) throws IOException {
        CommonActions.goToStudentProfilePage(event, this);
    }

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        CommonActions.goToLoginPage(event, this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        days = calendar.getChildren();
        try {
            fillCalendar("kurset_major", "major", Data.major);
            fillCalendar("kurset_minor", "minor", Data.minor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void fillCalendar(String tableName, String type, String category) throws SQLException {
        if(DB.connected()) {
            Statement stmt = DB.con.createStatement();
            String query = "SELECT * FROM "+tableName+" WHERE "+type+"= '"+category+"';";
            ResultSet rs = stmt.executeQuery(query);
            List<String> dates = new ArrayList<>(5);
            List<String> courseNames = new ArrayList<>(5);

            while(rs.next()) {
                String[] dateTime = getDate(rs.getTimestamp("koha_kursit"));
                String date = dateTime[0];
                String time = dateTime[1];
                String courseName = rs.getString("kurset_"+type);

                for(int i = 0; i < days.size(); i++) {
                    Node node = days.get(i);
                    if(((Label)((Pane)node).getChildren().get(0)).getText().equals(date)) {
                        ((ChoiceBox<String>)((Pane)node).getChildren().get(1)).getItems().add(courseName + " " + time);
                        ((Pane)node).setStyle("-fx-background-color: #ead154;");
                        break;
                    }
                }
            }
            rs.close();
            stmt.close();

        }
    }

    // Test
    private String[] getDate(Timestamp dateAndTime) {
        String sDT = dateAndTime.toString();
        String[] splited = sDT.split("\\s+");
        String[] splitedDate = splited[0].split("-");
        String[] dateTime = new String[2];
        dateTime[0] = splitedDate[2];
        dateTime[1] = splited[1];
        return dateTime;
    }
}

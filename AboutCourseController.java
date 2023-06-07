package com.example.studentfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AboutCourseController implements Initializable {
    public Button goBackBtn;
    public Button commentBtn;
    public Label userLabel, usernameLabel, initials;

    public Label dateLabel, timeLabel;

    public TableView<Attendee> attendeeTable;
    public TableColumn<Attendee, ImageView> attendeeImage;
    public TableColumn<Attendee, String> attendeeUsername, attendeeName, attendeeLastname;

    private ObservableList<Attendee> attendeeTableData = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);
        try {
            if(Data.type.equals("major")) {
                setMajorDateAndTime(dateLabel, timeLabel);
                setMajorAttendees();
            }
            else if(Data.type.equals("minor")) {
                setMinorDateAndTime(dateLabel, timeLabel);
                setMinorAttendees();
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        attendeeImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        attendeeUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        attendeeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        attendeeLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        attendeeTable.setItems(attendeeTableData);

    }

    // Kontrollo me vone per refactoring
    public void setMajorDateAndTime(Label date, Label time) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT koha_kursit FROM kurset_major WHERE kurset_major = '"+Data.courseName+"';";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            Timestamp dateAndTime = rs.getTimestamp("koha_kursit");
            String sDT = dateAndTime.toString();
            String[] splited = sDT.split("\\s+");
            date.setText(splited[0]);
            time.setText(splited[1]);
        }
        stmt.close();

    }
    public void setMinorDateAndTime(Label date, Label time) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT koha_kursit FROM kurset_minor WHERE kurset_minor = '"+Data.courseName+"';";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            Timestamp dateAndTime = rs.getTimestamp("koha_kursit");
            String sDT = dateAndTime.toString();
            String[] splited = sDT.split("\\s+");
            date.setText(splited[0]);
            time.setText(splited[1]);
        }
        stmt.close();
    }

    // Test?
    private void setMajorAttendees() throws SQLException{
        Statement stmt = DB.con.createStatement();
        String query = "SELECT username, name, surname FROM student WHERE username IN (SELECT username FROM kurset_stud_major WHERE lista_saved = true AND id_kurs_major IN (SELECT id_kurs_major FROM kurset_major WHERE major = '"+Data.major+"'));";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            attendeeTableData.add(
                    new Attendee(null, rs.getString("username"), rs.getString("name"), rs.getString("surname"))
            );
        }
    }

    // Test?
    private void setMinorAttendees() throws SQLException{
        Statement stmt = DB.con.createStatement();
        String query = "SELECT username, name, surname FROM student WHERE username IN (SELECT username FROM kurset_stud_minor WHERE lista_saved = true AND id_kurs_minor IN (SELECT id_kurs_minor FROM kurset_minor WHERE minor = '"+Data.minor+"'));";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            attendeeTableData.add(
                    new Attendee(null, rs.getString("username"), rs.getString("name"), rs.getString("surname"))
            );
        }
    }

    public void openCommentSection(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("comment-section.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(), 933, 585);
        stage.setScene(scene);
        stage.show();
    }

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

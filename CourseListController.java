package com.example.studentfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CourseListController implements Initializable {

    public Label userLabel, usernameLabel, initials;
    public TableView<Kursi> listMajor, listMinor, takenMajor, takenMinor;
    public TableColumn<Kursi, String> courseNameMajor, courseNameMinor, takenCourseNameMajor, takenCourseNameMinor;
    public TableColumn<Kursi, CheckBox> selectBtnMajor, selectBtnMinor, takenSelectBtnMajor, takenSelectBtnMinor;
    public ObservableList<Kursi> majorTableData = FXCollections.observableArrayList();
    public ObservableList<Kursi> minorTableData = FXCollections.observableArrayList();
    public ObservableList<Kursi> takenMajorTableData =  FXCollections.observableArrayList();
    public ObservableList<Kursi> takenMinorTableData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        try {
            addMajorData();
            addMinorData();
            showTakenMajorCourses();
            showTakenMinorCourses();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        courseNameMajor.setCellValueFactory(new PropertyValueFactory<Kursi, String>("courseName"));
        selectBtnMajor.setCellValueFactory(new PropertyValueFactory<Kursi, CheckBox>("select"));

        listMajor.setItems(majorTableData);

        courseNameMinor.setCellValueFactory(new PropertyValueFactory<Kursi, String>("courseName"));
        selectBtnMinor.setCellValueFactory(new PropertyValueFactory<Kursi, CheckBox>("select"));

        takenCourseNameMajor.setCellValueFactory(new PropertyValueFactory<Kursi, String>("courseName"));
        takenSelectBtnMajor.setCellValueFactory(new PropertyValueFactory<Kursi, CheckBox>("select"));

        takenCourseNameMinor.setCellValueFactory(new PropertyValueFactory<Kursi, String>("courseName"));
        takenSelectBtnMinor.setCellValueFactory(new PropertyValueFactory<Kursi, CheckBox>("select"));

        listMinor.setItems(minorTableData);
    }

    public void addMajorData() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT kurset_major FROM kurset_major WHERE id_kurs_major IN (SELECT id_kurs_major FROM kurset_stud_major WHERE lista_wish = true and lista_saved = false and username = '" + Data.text + "');";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            majorTableData.add(
                    new Kursi(rs.getString("kurset_major"))
            );
        }
    }

    public void addMinorData() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT kurset_minor FROM kurset_minor WHERE id_kurs_minor IN (SELECT id_kurs_minor FROM kurset_stud_minor WHERE lista_wish = true and username = '" + Data.text + "');";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            minorTableData.add(
                    new Kursi(rs.getString("kurset_minor"))
            );
        }
    }

    public void takeMajorCourse(ActionEvent event) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_major FROM kurset_stud_major WHERE lista_wish = true AND username = '" + Data.text + "';";
        ResultSet rs = stmt.executeQuery(query);
        int[] courseIds = new int[5];
        int i = 0;

        for(Kursi k : majorTableData) {
            if(k.getSelect().isSelected() && rs.next()) {
                takenMajorTableData.add(k);
                courseIds[i++] = rs.getInt("id_kurs_major");
            }
        }

        majorTableData.removeAll(takenMajorTableData);

        takenMajor.setItems(takenMajorTableData);


        for(int j = 0; j < i; j++) {
            String q = "UPDATE kurset_stud_major SET lista_saved = true, lista_wish = false WHERE id_kurs_major = " + courseIds[j] + " AND username = '" + Data.text + "';";
            stmt.execute(q);
        }

        stmt.close();
    }

    public void takeMinorCourse(ActionEvent event) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_minor FROM kurset_stud_minor WHERE lista_wish = true AND username = '" + Data.text + "';";
        ResultSet rs = stmt.executeQuery(query);
        int[] courseIds = new int[5];
        int i = 0;

        for(Kursi k : minorTableData) {
            if(k.getSelect().isSelected() && rs.next()) {
                takenMinorTableData.add(k);
                courseIds[i++] = rs.getInt("id_kurs_minor");
            }
        }

        minorTableData.removeAll(takenMinorTableData);

        takenMinor.setItems(takenMinorTableData);


        for(int j = 0; j < i; j++) {
            String q = "UPDATE kurset_stud_minor SET lista_saved = true, lista_wish = false WHERE id_kurs_minor = " + courseIds[j] + " AND username = '" + Data.text + "';";
            stmt.execute(q);
        }

        stmt.close();
    }

    // Test?
    public void showTakenMajorCourses() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT kurset_major FROM kurset_major WHERE id_kurs_major IN (SELECT id_kurs_major FROM kurset_stud_major WHERE lista_wish = false and lista_saved = true and username = '" + Data.text + "');";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            takenMajorTableData.add(
                    new Kursi(rs.getString("kurset_major"))
            );
        }

        takenMajor.setItems(takenMajorTableData);
        stmt.close();
    }

    // Test?
    public void showTakenMinorCourses() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT kurset_minor FROM kurset_minor WHERE id_kurs_minor IN (SELECT id_kurs_minor FROM kurset_stud_minor WHERE lista_wish = false and lista_saved = true and username = '" + Data.text + "');";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            takenMinorTableData.add(
                    new Kursi(rs.getString("kurset_minor"))
            );
        }

        takenMinor.setItems(takenMinorTableData);
        stmt.close();
    }

    public void removeMajorCourse(ActionEvent event) throws SQLException {

        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_major FROM kurset_stud_major WHERE lista_wish = true AND username = '" + Data.text + "';";
        ResultSet rs = stmt.executeQuery(query);
        int[] courseIds = new int[5];
        int i = 0;

        for(Kursi k : majorTableData) {
            if(k.getSelect().isSelected() && rs.next()) {
                courseIds[i++] = rs.getInt("id_kurs_major");
            }
        }

        for(int j = 0; j < i; j++) {
            String q = "UPDATE kurset_stud_major SET lista_wish = false WHERE id_kurs_major = " + courseIds[j] + " AND username = '" + Data.text + "';";
            stmt.execute(q);
        }
        stmt.close();
        listMajor.getItems().removeIf(k -> k.getSelect().isSelected());

    }

    public void removeMinorCourse(ActionEvent event) throws SQLException {

        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_minor FROM kurset_stud_minor WHERE lista_wish = true AND username = '" + Data.text + "';";
        ResultSet rs = stmt.executeQuery(query);
        int[] courseIds = new int[5];
        int i = 0;

        for(Kursi k : minorTableData) {
            if(k.getSelect().isSelected() && rs.next()) {
                courseIds[i++] = rs.getInt("id_kurs_minor");
            }
        }

        for(int j = 0; j < i; j++) {
            String q = "UPDATE kurset_stud_minor SET lista_wish = false WHERE id_kurs_minor = " + courseIds[j] + " AND username = '" + Data.text + "';";
            stmt.execute(q);
        }
        stmt.close();
        listMinor.getItems().removeIf(k -> k.getSelect().isSelected());

    }

    public void removeTakenMajorCourse(ActionEvent event) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_major FROM kurset_stud_major WHERE lista_saved = true AND username = '" + Data.text + "';";
        ResultSet rs = stmt.executeQuery(query);
        int[] courseIds = new int[5];
        int i = 0;

        for(Kursi k : takenMajorTableData) {
            if(k.getSelect().isSelected() && rs.next()) {
                courseIds[i++] = rs.getInt("id_kurs_major");
            }
        }

        for(int j = 0; j < i; j++) {
            String q = "UPDATE kurset_stud_major SET lista_saved = false WHERE id_kurs_major = " + courseIds[j] + " AND username = '" + Data.text + "';";
            stmt.execute(q);
        }
        stmt.close();
        takenMajor.getItems().removeIf(k -> k.getSelect().isSelected());
    }

    public void removeTakenMinorCourse(ActionEvent event) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_minor FROM kurset_stud_minor WHERE lista_saved = true AND username = '" + Data.text + "';";
        ResultSet rs = stmt.executeQuery(query);
        int[] courseIds = new int[5];
        int i = 0;

        for(Kursi k : takenMinorTableData) {
            if(k.getSelect().isSelected() && rs.next()) {
                courseIds[i++] = rs.getInt("id_kurs_minor");
            }
        }

        for(int j = 0; j < i; j++) {
            String q = "UPDATE kurset_stud_minor SET lista_saved = false WHERE id_kurs_minor = " + courseIds[j] + " AND username = '" + Data.text + "';";
            stmt.execute(q);
        }
        stmt.close();
        takenMinor.getItems().removeIf(k -> k.getSelect().isSelected());
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
package com.example.studentfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CommonActions {

    private static FXMLLoader loader;
    private static Scene scene;
    private static Stage stage;

    public static void goBack(ActionEvent event, String fxmlName, Object obj) throws IOException {
        loader = new FXMLLoader(obj.getClass().getResource(fxmlName));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load(),933, 585);
        stage.setScene(scene);
        stage.show();

        Data.stage = stage;
    }

    public static void goToLoginPage(ActionEvent event, Object obj) throws IOException {
        loader = new FXMLLoader(obj.getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load(),600, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void goToCourseListPage(ActionEvent event, Object obj) throws IOException {
        CommonActions.goBack(event, "course-list.fxml", obj);
    }

    public static void goToMyFriendsPage(ActionEvent event, Object obj) throws IOException {
        CommonActions.goBack(event, "my-friends.fxml", obj);
    }

    public static void goToCalendarPage(ActionEvent event, Object obj) throws IOException {
        CommonActions.goBack(event, "calendar.fxml", obj);
    }

    public static void goToNotificationsPage(ActionEvent event, Object obj) throws IOException{
        CommonActions.goBack(event, "notifications.fxml", obj);
    }

    public static void goToAnnouncementsPage(ActionEvent event, Object obj) throws IOException {
        CommonActions.goBack(event, "announcements.fxml", obj);
    }

    public static void goToStudentProfilePage(ActionEvent event, Object obj) throws IOException {
        CommonActions.goBack(event, "student-profile.fxml", obj);
    }

    public static void goToBrowseMyFriendsPage(ActionEvent event, Object obj) throws IOException {
        CommonActions.goBack(event, "browse-friends.fxml", obj);
    }


}

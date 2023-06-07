package com.example.studentfx;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static String text = "";
    public static String fullName = "";
    public static String username = "";
    public static String major = "";
    public static String minor = "";
    public static String initials = "";
    public static boolean notifications = true;
    public static List<String> majorCourseNames = new ArrayList<>(20);
    public static List<String> minorCourseNames = new ArrayList<>(20);
    public static String courseName = "";
    public static String type = "";
    public static Stage stage = null;

    // Perdoret per komunikim ndermjet klasave per te hequr elemente nga tabela e kerkesava pasi kemi
    // pranuar ose fshire kerkesen per shoqerim te nje personi
    public static TableView<FriendRequest> reqTable;

    public static TableView<User> findTable;
}

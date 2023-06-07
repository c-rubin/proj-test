package com.example.studentfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainSectionController implements Initializable {

    public Button kurset;
    public Label userLabel, usernameLabel, initials;
    public Button backBtn;
    public BorderPane mainSectionScene;
    public Tab majorTab, minorTab, findFriendsTab;
    public AnchorPane coursePaneMajor, coursePaneMinor;
    public Button calendarBtn;
    public TextField searchCourseMajorField, searchCourseMinorField, searchFriendsField;
    public Button searchFriendsBtn;
    public TableView<User> findFriendsTable;
    public TableColumn<User, ImageView> friendImage;
    public TableColumn<User, String> friendUsername;
    public TableColumn<User, String> friendName;
    public TableColumn<User, String> friendLastname;
    public TableColumn<User, Button> friendRequestBtn;
    private String[] majorSubjects = new String[5], minorSubjects = new String[5];
    private List<Pane> majorPanes, minorPanes;
    private ObservableList<User> findFriendsTableData;
    private FilteredList<User> filteredData;

    // Test
    public Pane createPane(String type, String text, int x, int y) throws SQLException {
        Pane p = new Pane();
        Button addBtn = addButton(type, text);
        Button aboutBtn = aboutButton(type, text);
        Label courseLabel = courseLabel(text);

        p.setPrefWidth(482);
        p.setPrefHeight(75);
        p.setLayoutX(x);
        p.setLayoutY(y);
        p.setStyle("-fx-background-color: #BBE1FA; -fx-background-radius: 10;");

        p.getChildren().add(courseLabel);
        p.getChildren().add(addBtn);
        p.getChildren().add(aboutBtn);

        return p;
    }

    // Test
    public Button aboutButton(String type, String courseName) {
        Button b = new Button("rreth kursit");
        b.setPrefWidth(94);
        b.setPrefHeight(36);
        b.setLayoutX(250);
        b.setLayoutY(20);
        b.setFont(new Font(14));
        b.setStyle("-fx-background-color: white; " +
                   "-fx-background-radius: 5; " +
                   "-fx-text-fill: #0f4c75; " +
                   "-fx-cursor: hand;");
        b.setOnAction(event -> {
            Data.type = type;
            Data.courseName = courseName;
            try {
                openAboutCoursePage(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return b;
    }

    // Test
    public Button addButton(String type, String courseName) throws SQLException {
        Button b = new Button("shto ne liste");
        b.setPrefWidth(94);
        b.setPrefHeight(36);
        b.setLayoutX(360);
        b.setLayoutY(20);
        b.setFont(new Font(14));
        b.setStyle("-fx-background-color: #0f4c75; " +
                    "-fx-background-radius: 5; " +
                    "-fx-text-fill: white; " +
                    "-fx-cursor: hand;");
        b.setOnAction(event -> {
            if(type.equals("major")) {
                try {
                    putInMajorCourseList(courseName);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(type.equals("minor")) {
                try {
                    putInMinorCourseList(courseName);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if(type.equals("major") && majorCourseTaken(courseName)) {
            b.setDisable(true);
        }
        if(type.equals("minor") && minorCourseTaken(courseName)) {
            b.setDisable(true);
        }
        return b;
    }

    // Test?
    public void putInMajorCourseList(String courseName) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_major FROM kurset_major WHERE kurset_major = '" + courseName + "';";
        ResultSet rs = stmt.executeQuery(query), rs2 = null;
        int courseId = 0;

        while(rs.next()) {
            courseId = rs.getInt("id_kurs_major");
        }

        stmt = DB.con.createStatement();
        query = "UPDATE kurset_stud_major SET lista_wish = true where id_kurs_major = " + courseId + " AND username = '" + Data.text + "';";
        stmt.execute(query);
        stmt.close();

    }

    // Test?
    public boolean majorCourseTaken(String courseName) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT lista_saved FROM kurset_stud_major WHERE username = '"+Data.text+"' AND id_kurs_major IN (SELECT id_kurs_major FROM kurset_major WHERE kurset_major = '"+courseName+"');";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            if(rs.getBoolean("lista_saved")) {
                stmt.close();
                return true;
            }
        }
        stmt.close();
        return false;
    }

    // Test?
    public boolean minorCourseTaken(String courseName) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT lista_saved FROM kurset_stud_minor WHERE username = '"+Data.text+"' AND id_kurs_minor IN (SELECT id_kurs_minor FROM kurset_minor WHERE kurset_minor = '"+courseName+"');";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            if(rs.getBoolean("lista_saved")) {
                stmt.close();
                return true;
            }
        }
        stmt.close();
        return false;
    }

    // Test?
    public void putInMinorCourseList(String courseName) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_minor FROM kurset_minor WHERE kurset_minor = '" + courseName + "';";
        ResultSet rs = stmt.executeQuery(query), rs2 = null;
        int courseId = 0;

        while(rs.next()) {
            courseId = rs.getInt("id_kurs_minor");
        }

        stmt = DB.con.createStatement();
        query = "UPDATE kurset_stud_minor SET lista_wish = true where id_kurs_minor = " + courseId + " AND username = '" + Data.text + "';";
        stmt.execute(query);
        stmt.close();

    }

    // Test
    public Label courseLabel(String text) {
        Label l = new Label(text);
        l.setFont(new Font(15));
        l.setLayoutX(41);
        l.setLayoutY(27);
        return l;
    }

    private void setMajorAndMinorCourses() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT kurset_major as kurset FROM kurset_major WHERE major = '" + Data.major +"' " +
                        " UNION SELECT kurset_minor FROM kurset_minor WHERE minor = '" + Data.minor +"';";
        ResultSet rs = stmt.executeQuery(query);

        for(int i = 0; i < 5 && rs.next(); i++) {
            majorSubjects[i] = rs.getString("kurset");
        }

        for(int i = 0; i < 5 && rs.next(); i++) {
            minorSubjects[i] = rs.getString("kurset");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DB.connect();
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        try {
            setMajorAndMinorCourses();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        majorPanes = new ArrayList<>(5);
        minorPanes = new ArrayList<>(5);

        int x = 14;
        for(int i = 0, y = 14; i < 5; i++) {
            try {
                majorPanes.add(createPane("major", majorSubjects[i], x, y));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            coursePaneMajor.getChildren().add(majorPanes.get(i));
            y+=91;
        }

        for(int i = 0, y = 14; i < 5; i++) {
            try {
                minorPanes.add(createPane("minor", minorSubjects[i], x, y));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            coursePaneMinor.getChildren().add(minorPanes.get(i));
            y+=91;
        }

        findFriendsTableData = FXCollections.observableArrayList();
        try {
            getFriends();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        friendImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        friendUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        friendName.setCellValueFactory(new PropertyValueFactory<>("name"));
        friendLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        friendRequestBtn.setCellValueFactory(new PropertyValueFactory<>("requestBtn"));

        findFriendsTable.setItems(findFriendsTableData);
        setFilteredData();

    }


    private void setFilteredData() {
        filteredData = new FilteredList<>(findFriendsTableData, b -> true);

        searchFriendsField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {

                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if(user.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if(user.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if(user.getLastname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else return false;
            });
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(findFriendsTable.comparatorProperty());
        findFriendsTable.setItems(sortedData);
    }

    public void getFriends() throws SQLException, IOException {
        if(DB.connected()) {
            Statement stmt = DB.con.createStatement();
            String query = "select username, name, surname, stud_foto " +
                    "from student where " +
                    "username not in " +
                    "(select marresi from shoket " +
                    "where dhenesi = '"+Data.text+"' ) " +
                    "and username not in " +
                    "(select dhenesi from friend_requests " +
                    "where gjendja = 'P') " +
                    "and username not in " +
                    "(select marresi from friend_requests " +
                    "where gjendja = 'P');";

            ResultSet rs = stmt.executeQuery(query);

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

                if(!rs.getString("username").equals(Data.text) && !rs.getString("username").equals("null")) {
                    findFriendsTableData.add(
                            new User(imageView, rs.getString("username"), rs.getString("name"), rs.getString("surname"))
                    );
                }
            }
            rs.close();
            stmt.close();
            Data.findTable = findFriendsTable;
        }
    }

    public void showCourse(KeyEvent keyEvent) {
        coursePaneMajor.getChildren().clear();
        for(int i = 0, y = 14; i < 5; i++) {
            majorPanes.get(i).setLayoutX(14);
            majorPanes.get(i).setLayoutY(y);
            coursePaneMajor.getChildren().add(majorPanes.get(i));
            y += 91;
            // 28 82
        }
    }

    public void searchCourseMajor(ActionEvent event) {
        Pane p = Search.findWord(majorPanes, searchCourseMajorField.getText());
        assert p != null;
        p.setLayoutX(14);
        p.setLayoutY(14);
        coursePaneMajor.getChildren().clear();
        coursePaneMajor.getChildren().add(p);
    }

    public void searchCourseMinor(ActionEvent event) {
        Pane p = Search.findWord(minorPanes, searchCourseMinorField.getText());
        assert p != null;
        p.setLayoutX(14);
        p.setLayoutY(14);
        coursePaneMinor.getChildren().clear();
        coursePaneMinor.getChildren().add(p);
    }

    private void showNotifications() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT * FROM friend_requests WHERE dhenesi = '"+Data.text+"';";
        ResultSet rs = stmt.executeQuery(query);
        String rejected = "Refuzimet\n\n";
        String accepted = "Pranimet\n\n";

        while(rs.next()) {
            String condition = rs.getString("gjendja");
            if(condition.equals("R")) {
                rejected += rs.getString("marresi") + " nuk e pranoi ftesen per shoqeri\n";
            }
            if(condition.equals("A")) {
                accepted += rs.getString("marresi") + " e pranoi ftesen per shoqeri\n";
            }
        }
        rs.close();
        stmt.close();

        if(Data.notifications) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Njoftimet");
            alert.setContentText(accepted + "\n" + rejected);
            alert.setHeaderText("Njoftimet");
            alert.showAndWait();
        }

    }

    public void goToCourseListPage(ActionEvent event) throws IOException {
        CommonActions.goToCourseListPage(event, this);
    }

    public void goToNotificationsPage(ActionEvent event) throws IOException {
        CommonActions.goToNotificationsPage(event, this);
        try {
            showNotifications();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToMyFriendsPage(ActionEvent event) throws IOException {
        CommonActions.goToMyFriendsPage(event, this);
    }

    public void goToCalendarPage(ActionEvent event) throws IOException {
        CommonActions.goToCalendarPage(event, this);
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
    
    @FXML
    private void openAboutCoursePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("about-course.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(),933, 585);
        stage.setScene(scene);
        stage.show();
    }
}

package com.example.studentfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CommentSectionController implements Initializable, Commenter {
    public Button goBackBtn;
    public TextArea commentTextArea;
    public Label userLabel, usernameLabel, initials;
    public TableView<Comment> commentTable;
    public TableColumn<Comment, String> usernameComTable;
    public TableColumn<Comment, String> forUserComTable;
    public TableColumn<Comment, String> commentComTable;
    public TableColumn<Comment, TextField> replyComTable;
    public TableColumn<Comment, TextField> replyBtn;
    public TableColumn<Comment, Button> likeBtn;

    public ObservableList<Comment> tableData = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(Data.fullName);
        usernameLabel.setText(Data.text);
        initials.setText(Data.initials);

        if(Data.type.equals("major")) {
            try {
                getCommentsMajor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(Data.type.equals("minor")) {
            try {
                getCommentsMinor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        usernameComTable.setCellValueFactory(new PropertyValueFactory<>("username"));
        forUserComTable.setCellValueFactory(new PropertyValueFactory<>("forUser"));
        commentComTable.setCellValueFactory(new PropertyValueFactory<>("comment"));
        replyComTable.setCellValueFactory(new PropertyValueFactory<>("replyText"));
        replyBtn.setCellValueFactory(new PropertyValueFactory<>("replyBtn"));
        likeBtn.setCellValueFactory(new PropertyValueFactory<>("likeBtn"));

        commentTable.setItems(tableData);

        ReplyData.setData(tableData);
    }

    // Test?
    @Override
    public void publish(String comment, String tableName1, String id, String tableName2, String type, String category) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT "+id+" FROM "+tableName1+" WHERE "+tableName1+" = '"+Data.courseName+"' AND "+type+" = '"+category+"';";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            PreparedStatement pstmt = DB.con.prepareStatement("INSERT INTO "+tableName2+" (username_sender, username_reciever, id_kursit, komenti, pelqimi) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, Data.text);
            pstmt.setString(2, "null");
            pstmt.setInt(3, rs.getInt(id));
            pstmt.setString(4, comment);
            pstmt.setInt(5, 0);
            pstmt.execute();
            pstmt.close();
        }
        stmt.close();
    }


    // Test?
    public void getCommentsMajor() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT username_sender, username_reciever, komenti, pelqimi FROM komentet_major WHERE id_kursit IN (SELECT id_kurs_major FROM kurset_major WHERE kurset_major = '"+Data.courseName+"');";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            tableData.add(
                    new Comment(rs.getString("username_sender"), rs.getString("username_reciever"), rs.getString("komenti"), rs.getInt("pelqimi"))
            );
        }
    }

    // Test?
    public void getCommentsMinor() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT username_sender, username_reciever, komenti, pelqimi FROM komentet_minor WHERE id_kursit IN (SELECT id_kurs_minor FROM kurset_minor WHERE kurset_minor = '"+Data.courseName+"');";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            tableData.add(
                    new Comment(rs.getString("username_sender"), rs.getString("username_reciever"), rs.getString("komenti"), rs.getInt("pelqimi"))
            );
        }
    }


    public void goBack(ActionEvent event) throws IOException {
        CommonActions.goBack(event,"about-course.fxml", this);
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


    public void postComment(ActionEvent event) throws SQLException{
        String myComment = commentTextArea.getText();
        Comment c = new Comment(Data.text, "", myComment, 0);
        tableData.add(c);
        if(Data.type.equals("major")) {
            publish(myComment, "kurset_major", "id_kurs_major", "komentet_major", "major", Data.major);
        }
        else if(Data.type.equals("minor")) {
            publish(myComment, "kurset_minor", "id_kurs_minor", "komentet_minor", "minor", Data.major);
        }

    }

}

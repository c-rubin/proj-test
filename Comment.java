package com.example.studentfx;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Comment implements Commenter {
    private String username;
    private String forUser;
    private String comment;
    private TextField replyText;
    private Button replyBtn;
    private Button likeBtn;

    public Comment(String username, String forUser, String comment, int likeNum) {
        this.username = username;
        this.forUser = forUser;
        this.comment = comment;
        this.replyText = new TextField();
        this.replyBtn = createReplyButton();
        this.likeBtn = createLikeBtn(likeNum);
    }

    private Button createLikeBtn(int likeNum) {
        Button lc = new Button(""+likeNum);
        lc.setStyle("-fx-background-color:  #E74C3C; -fx-text-fill: white; -fx-cursor: hand;");
        lc.setOnAction(event -> {
            try {
                if(Data.type.equals("major")) {
                    updateLike("komentet_major");
                }
                else if(Data.type.equals("minor")) {
                    updateLike("komentet_minor");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return lc;
    }

    // Test?
    private Button createReplyButton() {
        Button rb = new Button("reply");
        rb.setStyle("-fx-background-color:  #0f4c75; -fx-text-fill: white; -fx-cursor: hand;");
        rb.setOnAction(event -> {
            String myComment = this.comment + "-> \n" + this.replyText.getText();
            ReplyData.setReplyData(Data.text, this.username, myComment);
            try {
                if(Data.type.equals("major")) {
                    publish(myComment, "kurset_major", "id_kurs_major", "komentet_major", "major", Data.major);
                }
                else if(Data.type.equals("minor")) {
                    publish(myComment, "kurset_minor", "id_kurs_minor", "komentet_minor", "minor", Data.minor);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return rb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TextField getReplyText() {
        return replyText;
    }

    public void setReplyText(TextField replyText) {
        this.replyText = replyText;
    }

    public String getForUser() {
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }

    public Button getReplyBtn() {
        return replyBtn;
    }

    public void setReplyBtn(Button replyBtn) {
        this.replyBtn = replyBtn;
    }

    public Button getLikeBtn() {
        return likeBtn;
    }

    public void setLikeBtn(Button likeBtn) {
        this.likeBtn = likeBtn;
    }

    // Test? id_kurs_major, kurset_major, komentet_major
    @Override
    public void publish(String comment, String tableName1, String id, String tableName2, String type, String category) throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT "+id+" FROM "+tableName1+" WHERE "+tableName1+" = '"+Data.courseName+"' AND "+type+" = '"+category+"';";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            PreparedStatement pstmt = DB.con.prepareStatement("INSERT INTO "+tableName2+" (username_sender, username_reciever, id_kursit, komenti, pelqimi) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, Data.text);
            pstmt.setString(2, this.username);
            pstmt.setInt(3, rs.getInt(id));
            pstmt.setString(4, comment);
            pstmt.setInt(5, Integer.parseInt(this.likeBtn.getText()));
            pstmt.execute();
            pstmt.close();
        }
        stmt.close();
    }

    // Test?
    public void updateLike(String tableName) throws SQLException {
        Statement stmt = DB.con.createStatement();
        int numberOfLikes = Integer.parseInt(this.likeBtn.getText());
        numberOfLikes += 1;
        this.likeBtn.setText(numberOfLikes + "");

        String query = "UPDATE "+tableName+" SET pelqimi = "+this.likeBtn.getText()+" WHERE username_sender = '"+this.username+"' AND username_reciever = '"+this.forUser+"' AND komenti = '"+this.comment+"';";
        stmt.execute(query);
    }
}

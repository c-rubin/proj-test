package com.example.studentfx;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable{


    public TextField usernameLogIn;
    public TextField passwordLogIn;
    public Button loginBtn;
    public Button signUpBtn;
    public BorderPane logInScene;

    FXMLLoader loader;
    Stage stage;
    Scene scene;
    List<TextField> textFields;

    // Metodat publike
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DB.connect();
        if(!Data.username.isEmpty()) {
            usernameLogIn.setText(Data.username);
        }
        textFields = new ArrayList<>(2);
        textFields.add(usernameLogIn);
        textFields.add(passwordLogIn);
    }
    public void logIn(ActionEvent event) throws IOException, SQLException {
        if(DB.connected()) {
            if(registered()) {
                setMajorAndMinor();
                System.out.println(Data.major);
                System.out.println(Data.minor);
                Data.text = usernameLogIn.getText();
                DB.getFullName();
                loader = new FXMLLoader(getClass().getResource("main-section.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(loader.load(),933, 585);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    public boolean registered() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT * FROM student WHERE username = '" + usernameLogIn.getText() +"';";
        ResultSet rs = stmt.executeQuery(query);

        if(!rs.isBeforeFirst()) {
            wrongField(usernameLogIn);
        }
        while(rs.next()) {
            correctField(usernameLogIn);
            String username = rs.getString("username");
            String password = rs.getString("password");
            if(password.equals(passwordLogIn.getText())) {
                correctUserAndPass();
                return true;
            }
            else {
                wrongField(passwordLogIn);
                return false;
            }
        }
        return false;
    }

    // Test
    public void wrongField(TextField textField) {
        textField.setStyle("-fx-border-radius: 2; -fx-border-color: red;");
    }

    // Test
    public void correctField(TextField textField) {
        textField.setStyle("-fx-border-radius: 2; -fx-border-color: green;");
    }

    // Test
    public void correctUserAndPass() {
        textFields.forEach(textField -> {
            textField.setStyle("-fx-border-radius: 2; -fx-border-color: green;");
        });
    }

    public void setMajorAndMinor() throws SQLException {
        if(DB.connected()) {
            Statement stmt = DB.con.createStatement();
            String query = "SELECT major, minor FROM student WHERE username = '" + usernameLogIn.getText() +"';";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                Data.major = rs.getString("major");
                Data.minor = rs.getString("minor");
            }
        }
    }
    public void onSignUpBtnClick(ActionEvent event) throws IOException{
        loader = new FXMLLoader(getClass().getResource("sign-up.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load(), 600, 400);
        stage.setScene(scene);
        stage.show();
    }

}
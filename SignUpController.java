package com.example.studentfx;

import javafx.beans.binding.ObjectExpression;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SignUpController implements Initializable {


    public ComboBox<String> majorOptions, minorOptions;
    public TextField username, emri, mbiemri, password;
    public Button backBtn, signUpBtn2;

    // Variablat private
    private List<TextField> textFields;
    private List<ComboBox<String>> options;
    private ObservableList<String> comboBoxItems;
    private String[] majorItems, minorItems;
    private HashMap<Object, Boolean> checkValues;

    // Metodat private
    private boolean checkFilled() {
        AtomicBoolean checkTextFields = new AtomicBoolean(true);
        AtomicBoolean checkOptions = new AtomicBoolean(true);

        textFields.forEach(textField -> {
            if(textField.getText().isEmpty()) {
                textField.setStyle("-fx-border-radius: 2; -fx-border-color: red");
                checkTextFields.set(false);
            }
        });

        options.forEach(option -> {
            if(option.getValue() == null) {
                option.setStyle("-fx-border-radius: 2; -fx-border-color: red");
                checkOptions.set(false);
            }
        });

        return checkTextFields.get() && checkOptions.get();
    }

    // Metodat publike
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DB.connect();
        majorItems = new String[]{"Inxhinieri Informatike", "Inxhinieri Elektronike", "Inxhinieri Telekomunikacioni", "Inxhinieri Mekanike", "Inxhinieri Ndertimi"};
        minorItems = new String[]{"Ekonomi", "Matematike", "Informatike", "Fizike", "Kimi"};

        majorOptions.getItems().setAll(majorItems);
        minorOptions.getItems().setAll(minorItems);

//        comboBoxItems = majorOptions.getItems();

        textFields = new ArrayList<>(4);
        options = new ArrayList<>(2);
        textFields.add(emri);
        textFields.add(mbiemri);
        textFields.add(password);
        options.add(majorOptions);
        options.add(minorOptions);

        checkValues = new HashMap<>(6);
        checkValues.put(username, false);
        checkValues.put(emri, false);
        checkValues.put(mbiemri, false);
        checkValues.put(password, false);
        checkValues.put(majorOptions, false);
        checkValues.put(minorOptions, false);

    }

    public void validateUserName(KeyEvent keyEvent) {
        try {
            if(!username.getText().isEmpty() && DB.checkIfNew("student", username.getText())) {
                username.setStyle("-fx-border-radius: 2; -fx-border-color: green");
                checkValues.replace(username, true);
            }
            else {
                username.setStyle("-fx-border-radius: 2; -fx-border-color: red");
                checkValues.replace(username, false);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void validate(KeyEvent keyEvent) {
        TextField t = (TextField) keyEvent.getSource();
        if(!t.getText().isEmpty()) {
            t.setStyle("-fx-border-radius: 2; -fx-border-color: green");
            checkValues.replace(t, true);
        }
        else {
            t.setStyle("-fx-border-radius: 2; -fx-border-color: red");
            checkValues.replace(t, false);
        }
    }

    public void validateMajorOptions(ActionEvent event) {
        String selected = "";
        if((selected = majorOptions.getValue()) != null) {
            majorOptions.setStyle("-fx-border-radius: 2; -fx-border-color: green");
            checkValues.replace(majorOptions, true);
        }
    }

    public void validateMinorOptions(ActionEvent event) {
        String selected = "";
        if((selected = minorOptions.getValue()) != null) {
            minorOptions.setStyle("-fx-border-radius: 2; -fx-border-color: green");
            checkValues.replace(minorOptions, true);
        }
    }

    // Test
    public boolean checkTrueValues(HashMap<Object, Boolean> hm) {
        AtomicBoolean result = new AtomicBoolean(true);
        hm.forEach((key, value) -> {
            if(!value) {
                result.set(false);
            }
        });
        return result.get();
    }


    public void signUp(ActionEvent event) throws IOException, SQLException {
        if(checkFilled() && checkTrueValues(checkValues)) {
            if(DB.connected()) {
                PreparedStatement stmt = DB.con.prepareStatement("INSERT INTO student (username, name, surname, password, major, minor, stud_foto) VALUES(?, ?, ?, ?, ?, ?, ?)");
                stmt.setString(1, username.getText());
                stmt.setString(2, textFields.get(0).getText());
                stmt.setString(3,textFields.get(1).getText());
                stmt.setString(4, textFields.get(2).getText());
                stmt.setString(5, options.get(0).getValue());
                stmt.setString(6, options.get(1).getValue());
                stmt.setString(7, null);
                stmt.execute();
                stmt.close();
                setUpMajorCourses();
                setUpMinorCourses();
            }
            Data.username = username.getText();
            CommonActions.goToLoginPage(event, this);
        }

    }

    private void setUpMajorCourses() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_major FROM kurset_major WHERE major = '"+options.get(0).getValue()+"';";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            PreparedStatement pstmt = DB.con.prepareStatement("INSERT INTO kurset_stud_major (username, id_kurs_major) VALUES (?, ?)");
            pstmt.setString(1, username.getText());
            pstmt.setString(2, rs.getString("id_kurs_major"));
            pstmt.execute();
            pstmt.close();
        }
    }

    private void setUpMinorCourses() throws SQLException {
        Statement stmt = DB.con.createStatement();
        String query = "SELECT id_kurs_minor FROM kurset_minor WHERE minor = '"+options.get(1).getValue()+"';";
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()) {
            PreparedStatement pstmt = DB.con.prepareStatement("INSERT INTO kurset_stud_minor (username, id_kurs_minor) VALUES (?, ?)");
            pstmt.setString(1, username.getText());
            pstmt.setString(2, rs.getString("id_kurs_minor"));
            pstmt.execute();
            pstmt.close();
        }
    }

    public void goBack(ActionEvent event) throws IOException {
        CommonActions.goToLoginPage(event, this);
    }

}

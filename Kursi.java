package com.example.studentfx;

import javafx.scene.control.CheckBox;

public class Kursi {
    private String courseName;
    private CheckBox select;

    public Kursi(String courseName) {
        this.courseName = courseName;
        this.select = new CheckBox();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }
}

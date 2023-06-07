package com.example.studentfx;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.List;

public class Search {

    // Test
    public static Pane findWord(List<Pane> panes, String word) {
        for(Pane pane : panes) {
            ObservableList<Node> list = pane.getChildren();
            if( ((Label)list.get(0)).getText().equals(word)) {
                return pane;
            }
        }
        return null;

    }
}

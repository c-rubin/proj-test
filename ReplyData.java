package com.example.studentfx;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;

public class ReplyData {

    private static String rComment = "";
    private static String rUser = "";
    private static ObservableList<Comment> data;

    public static void setData(ObservableList<Comment> data) {
        ReplyData.data = data;
    }

    public static void setReplyData(String rUser, String rForUser, String rComment) {
        CheckBox likeBtn = new CheckBox();
        Comment c = new Comment(rUser, rForUser, rComment, 0);
        data.add(c);
    }

    public static String[] getReplyData() {
        String[] s = new String[2];
        s[0] = ReplyData.rComment;
        s[1] = ReplyData.rUser;

        return s;
    }

}

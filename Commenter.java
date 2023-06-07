package com.example.studentfx;

import java.sql.SQLException;

public interface Commenter {

    // Test?
    void publish(String comment, String tableName1, String id, String tableName2, String type, String category) throws SQLException;
}

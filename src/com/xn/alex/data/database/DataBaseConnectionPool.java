package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataBaseConnectionPool {
    public Connection getConnection() throws SQLException;
    DataBaseConnectionPool getMySqlDataBaseConnectionPool();
}

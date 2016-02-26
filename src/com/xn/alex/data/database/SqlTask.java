package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public interface SqlTask {
    public Statement run(final Connection connection) throws SQLException;
}

package io.shnflrsc.HotelPosSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final Connection connection;

    public Database() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:hotel_pos.db");
        this.connection.createStatement().execute("PRAGMA foreign_keys = ON");
    }

    public Connection connect() {
        return connection;
    }
}

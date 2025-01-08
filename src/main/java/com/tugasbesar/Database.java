package com.tugasbesar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:clinic.db"; // Nama database

    public static void main(String[] args) {
        Database database = new Database();
        database.connectDatabase();
        database.createTable();
    }

    // Metode untuk membuat koneksi ke database
    private void connectDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            if (connection != null) {
                System.out.println("Connected to the database.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Metode untuk membuat tabel pasien
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS patient (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "name TEXT NOT NULL," +
                     "age INTEGER NOT NULL," +
                     "address TEXT NOT NULL" +
                     ");";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'patient' created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
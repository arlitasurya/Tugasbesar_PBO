package com.tugasbesar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class DoctorController {
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtSpesialis;
    @FXML
    private TableView<Doctor> tableDoctor;
    @FXML
    private TableColumn<Doctor, String> colNama;
    @FXML
    private TableColumn<Doctor, Integer> colSpesialis;
    @FXML
    private TableColumn<Doctor, String> colAddress;

    private Connection connection;
    private ObservableList<Doctor> DoctorList;

    // Model Patient
    public static class Doctor {
        private int id;
        private String nama;
        private String spesialis;

        public Doctor(int id, String nama, String spesialis) {
            this.id = id;
            this.nama = nama;
            this.spesialis = spesialis;
        }

        public int getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }

        public String getSpesialis() {
            return spesialis;
        }
    }

    public void initialize() {
        DoctorList = FXCollections.observableArrayList();
        connectDatabase();
        setupTableColumns();
        loadData();
        eventHandler();
    }

    private void connectDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:clinic.db");
            System.out.println("connection successfull");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colSpesialis.setCellValueFactory(new PropertyValueFactory<>("spesialis"));
    
    }

    private void loadData() {
        DoctorList.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM doctors");
            while (resultSet.next()) {
                DoctorList
                        .add(new Doctor(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("specialty")
                             ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableDoctor.setItems(DoctorList);
    }

    private void eventHandler() {
        tableDoctor.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Menggunakan single click untuk memilih
                Doctor selectedPatient = tableDoctor.getSelectionModel().getSelectedItem();
                if (selectedPatient != null) {
                    // Memasukkan data ke TextField
                    txtNama.setText(selectedPatient.getNama());
                    txtSpesialis.setText(selectedPatient.getSpesialis());
                }
            }
        });
    }

    @FXML
    private void addDoctor() {
        String name = txtNama.getText();
        String specialty = txtSpesialis.getText();

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO doctors (name, specialty) VALUES (?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, specialty);
            preparedStatement.executeUpdate();
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteDoctor() {
        Doctor selectedDoctor = tableDoctor.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM doctors WHERE id = ?");
                preparedStatement.setInt(1, selectedDoctor.getId());
                preparedStatement.executeUpdate();
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void editDoctor() {
        Doctor selectedDoctor = tableDoctor.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            // Mengambil data dari TextField
            String newNama = txtNama.getText();
            String newSpesialis= txtSpesialis.getText();

            try {
                // Memperbarui data dokter di database
                PreparedStatement preparedStatement = connection
                        .prepareStatement("UPDATE doctors SET name = ?, specialty = ? WHERE id = ?");
                preparedStatement.setString(1, newNama);
                preparedStatement.setString(2, newSpesialis);
                preparedStatement.setInt(3, selectedDoctor.getId());
                preparedStatement.executeUpdate();

                // Memuat ulang data setelah edit
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
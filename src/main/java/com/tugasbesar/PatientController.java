package com.tugasbesar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class PatientController {
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField txtAddress;
    @FXML
    private TableView<Patient> tablePatient;
    @FXML
    private TableColumn<Patient, String> colName;
    @FXML
    private TableColumn<Patient, Integer> colAge;
    @FXML
    private TableColumn<Patient, String> colAddress;

    private Connection connection;
    private ObservableList<Patient> PatientList;

    // Model Patient
    public static class Patient {
        private int id;
        private String name;
        private int age;
        private String address;

        public Patient(int id, String name, int age, String address) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.address = address;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getAddress() {
            return address;
        }
    }

    public void initialize() {
        PatientList = FXCollections.observableArrayList();
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
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("Age"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void loadData() {
        PatientList.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patient");
            while (resultSet.next()) {
                PatientList.add(new Patient(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("Age"),
                        resultSet.getString("address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tablePatient.setItems(PatientList);
    }

    private void eventHandler(){
        tablePatient.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Menggunakan single click untuk memilih
                Patient selectedPatient = tablePatient.getSelectionModel().getSelectedItem();
                if (selectedPatient != null) {
                    // Memasukkan data ke TextField
                    txtName.setText(selectedPatient.getName());
                    txtAge.setText(String.valueOf(selectedPatient.getAge())); // Mengonversi int ke String
                    txtAddress.setText(selectedPatient.getAddress());
                }
            }
        });
    }

    @FXML
    private void addPatient() {
        String name = txtName.getText();
        int Age = Integer.parseInt(txtAge.getText());
        String address = txtAddress.getText();

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO patient (name, Age, address) VALUES (?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, Age);
            preparedStatement.setString(3, address);
            preparedStatement.executeUpdate();
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deletePatient() {
        Patient selectedPatient = tablePatient.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM patient WHERE id = ?");
                preparedStatement.setInt(1, selectedPatient.getId());
                preparedStatement.executeUpdate();
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void editPatient() {
        Patient selectedPatient = tablePatient.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Mengambil data dari TextField
            String newName = txtName.getText();
            int newAge;
            String newAddress = txtAddress.getText();

            // Mengonversi umur dari String ke int dan menangani kesalahan
            try {
                newAge = Integer.parseInt(txtAge.getText());
            } catch (NumberFormatException e) {
                return; // Keluar dari metode jika umur tidak valid
            }

            try {
                // Memperbarui data pasien di database
                PreparedStatement preparedStatement = connection
                        .prepareStatement("UPDATE patient SET name = ?, age = ?, address = ? WHERE id = ?");
                preparedStatement.setString(1, newName);
                preparedStatement.setInt(2, newAge);
                preparedStatement.setString(3, newAddress);
                preparedStatement.setInt(4, selectedPatient.getId());
                preparedStatement.executeUpdate();

                // Memuat ulang data setelah edit
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
    }
}
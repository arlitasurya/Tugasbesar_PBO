package com.tugasbesar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class PrimaryController {
    @FXML
    private TextField txtDokter;
    @FXML
    private TextField txtPasien;
    @FXML
    private TextField txtTanggal;
    @FXML
    private TextField txtWaktu;
    @FXML
    private TextField txtNotes;

     private Connection connection;

    @FXML
    private void switchToDoctor(ActionEvent event) {
        loadPage("doctor.fxml", "Halaman Dokter", event);
    }

    @FXML
    private void switchToPatient(ActionEvent event) {
        loadPage("patient.fxml", "Halaman Dokter", event);
    }

    @FXML
    private void switchToAppointment(ActionEvent event) {
        loadPage("appointment.fxml", "Halaman Dokter", event);
    }

    private void loadPage(String fxmlFile, String title, ActionEvent event) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource("/com/tugasbesar/" + fxmlFile));
            Stage stage = (Stage) ((Node) ((MenuItem) event.getSource()).getParentPopup().getOwnerNode()).getScene().getWindow();
            stage.setScene(new Scene(page, 400, 300));
            stage.setScene(new Scene(page, 400, 300));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        connectDatabase();
    }

    private void connectDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:clinic.db");
            System.out.println("connection successfull");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tambahJadwal() {
        String patientId = txtPasien.getText();
        String doctorId = txtDokter.getText();
        String date = txtTanggal.getText();
        String time = txtWaktu.getText();
        String notes = txtNotes.getText();

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO appointments (patientId, doctorId, date, time, notes) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, patientId);
            preparedStatement.setString(2, doctorId);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, time);
            preparedStatement.setString(5, notes);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

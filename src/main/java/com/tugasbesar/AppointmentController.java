package com.tugasbesar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class AppointmentController {
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

    @FXML
    private TableView<Appointment> tableAppointment;
    @FXML
    private TableColumn<Appointment, Integer> colPatient_id;
    @FXML
    private TableColumn<Appointment, Integer> colDoctor_id;
    @FXML
    private TableColumn<Appointment, String> colDate;
    @FXML
    private TableColumn<Appointment, String> colTime;
    @FXML
    private TableColumn<Appointment, String> colNotes;

    private Connection connection;
    private ObservableList<Appointment> AppointmentList;

    // Model Patient
    public static class Appointment {
        private int id;
        private Integer pasienId;
        private Integer dokterId;
        private String tanggal;
        private String waktu;
        private String notes;

        public Appointment(int id, int pasienId, int dokterId, String tanggal, String waktu, String notes) {
            this.id = id;
            this.dokterId = dokterId;
            this.pasienId = pasienId;
            this.tanggal = tanggal;
            this.waktu = waktu;
            this.notes = notes;
        }

        public int getId() {
            return id;
        }

        
        public int getpasienId() {
            return pasienId;
        }

        public int getdokterId() {
            return dokterId;
        }

        public String getTanggal(){
            return tanggal;
        }

        public String getWaktu(){
            return waktu;
        }

        public String getNotes(){
            return notes;
        }
    }

    public void initialize() {
        AppointmentList = FXCollections.observableArrayList();
        connectDatabase();
        setupTableColumns();
        loadData();
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
        colPatient_id.setCellValueFactory(new PropertyValueFactory<>("patient_id"));
        colDoctor_id.setCellValueFactory(new PropertyValueFactory<>("doctor_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

    }

    private void loadData() {
        AppointmentList.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments");
            while (resultSet.next()) {
                AppointmentList
                        .add(new Appointment(resultSet.getInt("id"), resultSet.getInt("patient_id"),
                                resultSet.getInt("doctor_id"),resultSet.getString("date"),resultSet.getString("time"),resultSet.getString("notes")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableAppointment.setItems(AppointmentList);
    }
}
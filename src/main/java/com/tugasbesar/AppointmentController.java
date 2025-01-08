package com.tugasbesar;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentController {
    @FXML
    private TextField idField;
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField doctorIdField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField timeField;
    @FXML
    private TextField notesField;
    @FXML
    private ListView<Appointment> appointmentListView;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        appointmentListView.setItems(appointments);
        appointmentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                idField.setText(String.valueOf(newValue.getId()));
                patientIdField.setText(String.valueOf(newValue.getPatientId()));
                doctorIdField.setText(String.valueOf(newValue.getDoctorId()));
                dateField.setText(newValue.getDate().toString());
                timeField.setText(newValue.getTime().toString());
                notesField.setText(newValue.getNotes());
            }
        });
    }

    @FXML
    private void addAppointment() {
        int id = Integer.parseInt(idField.getText());
        int patientId = Integer.parseInt(patientIdField.getText());
        int doctorId = Integer.parseInt(doctorIdField.getText());
        LocalDate date = LocalDate.parse(dateField.getText());
        LocalTime time = LocalTime.parse(timeField.getText());
        String notes = notesField.getText();
        appointments.add(new Appointment(id, patientId, doctorId, date, time, notes));
        clearFields();
    }

    @FXML
    private void updateAppointment() {
        Appointment selectedAppointment = appointmentListView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            selectedAppointment.setPatientId(Integer.parseInt(patientIdField.getText()));
            selectedAppointment.setDoctorId(Integer.parseInt(doctorIdField.getText()));
            selectedAppointment.setDate(LocalDate.parse(dateField.getText()));
            selectedAppointment.setTime(LocalTime.parse(timeField.getText()));
            selectedAppointment.setNotes(notesField.getText());
            appointmentListView.refresh();
            clearFields();
        }
    }

    @FXML
    private void deleteAppointment() {
        Appointment selectedAppointment = appointmentListView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            appointments.remove(selectedAppointment);
            clearFields();
        }
    }

    private void clearFields() {
        idField.clear();
        patientIdField.clear();
        doctorIdField.clear();
        dateField.clear();
        timeField.clear();
        notesField.clear();
    }

    public static class Appointment {
        private int id;
        private int patientId;
        private int doctorId;
        private LocalDate date;
        private LocalTime time;
        private String notes;

        public Appointment(int id, int patientId, int doctorId, LocalDate date, LocalTime time, String notes) {
            this.id = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.date = date;
            this.time = time;
            this.notes = notes;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPatientId() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId = patientId;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public LocalTime getTime() {
            return time;
        }

        public void setTime(LocalTime time) {
            this.time = time;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}
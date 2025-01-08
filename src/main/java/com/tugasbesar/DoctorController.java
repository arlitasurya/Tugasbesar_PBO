package com.tugasbesar;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;

public class DoctorController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField specialtyField;
    @FXML
    private ListView<Doctor> doctorListView;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        doctorListView.setItems(doctors);
        doctorListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameField.setText(newValue.getName());
                specialtyField.setText(newValue.getSpecialty());
            }
        });
    }

    @FXML
    private void addDoctor() {
        String name = nameField.getText();
        String specialty = specialtyField.getText();
        doctors.add(new Doctor(name, specialty));
        clearFields();
    }

    @FXML
    private void updateDoctor() {
        Doctor selectedDoctor = doctorListView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            selectedDoctor.setName(nameField.getText());
            selectedDoctor.setSpecialty(specialtyField.getText());
            doctorListView.refresh();
            clearFields();
        }
    }

    @FXML
    private void deleteDoctor() {
        Doctor selectedDoctor = doctorListView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            doctors.remove(selectedDoctor);
            clearFields();
        }
    }

    private void clearFields() {
        nameField.clear();
        specialtyField.clear();
    }

    public static class Doctor {
        private String name;
        private String specialty;

        public Doctor(String name, String specialty) {
            this.name = name;
            this.specialty = specialty;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        @Override
        public String toString() {
            return name + " (" + specialty + ")";
        }
    }
}

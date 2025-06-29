package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import models.Appointment;

import java.time.LocalDate;
import java.util.List;

public class AppointmentController {

    @FXML private TextField doctorField;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextField reasonField;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    @FXML private ListView<String> appointmentList;
    @FXML private ComboBox<String> filterComboBox;

    private final ObservableList<String> appointments = FXCollections.observableArrayList();
    private List<Appointment> loadedAppointments;

    @FXML
    public void initialize() {
        filterComboBox.setItems(FXCollections.observableArrayList("Today", "Next 7 Days", "Next 30 Days", "All"));
        filterComboBox.setValue("Today");

        filterComboBox.setOnAction(e -> loadAppointments());

        searchField.textProperty().addListener((obs, oldText, newText) -> applySearchFilter());

        loadAppointments();
    }

    @FXML
    private void loadAppointments() {
        appointments.clear();
        LocalDate today = LocalDate.now();

        switch (filterComboBox.getValue()) {
            case "Today" -> loadedAppointments = Appointment.getAppointmentsForDate(today);
            case "Next 7 Days" -> loadedAppointments = Appointment.getAppointmentsBetween(today, today.plusDays(7));
            case "Next 30 Days" -> loadedAppointments = Appointment.getAppointmentsBetween(today, today.plusDays(30));
            case "All" -> loadedAppointments = Appointment.getAllAppointments();
        }

        applySearchFilter();
    }

    private void applySearchFilter() {
        String keyword = searchField.getText().toLowerCase();
        appointments.clear();
        for (Appointment a : loadedAppointments) {
            if (keyword.isEmpty() || a.getDoctor().toLowerCase().contains(keyword) || a.getReason().toLowerCase().contains(keyword)) {
                appointments.add(a.getDoctor() + " - " + a.getDate() + " at " + a.getTime() + " | " + a.getReason());
            }
        }
        appointmentList.setItems(appointments);
    }

    @FXML
    private void addAppointment() {
        String doctor = doctorField.getText();
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        String time = timeField.getText();
        String reason = reasonField.getText();

        if (doctor.isEmpty() || date.isEmpty() || time.isEmpty()) {
            statusLabel.setText("Please fill all required fields.");
            return;
        }

        Appointment.saveAppointment(new Appointment(0, doctor, date, time, reason));
        statusLabel.setText("Appointment added!");

        clearForm();

        // Show all appointments so new one appears
        filterComboBox.setValue("All");
        loadAppointments();
    }

    @FXML
    private void deleteSelectedAppointment() {
        int selectedIndex = appointmentList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < loadedAppointments.size()) {
            Appointment selected = loadedAppointments.get(selectedIndex);

            Alert confirm = new Alert(AlertType.CONFIRMATION, "Delete appointment for " + selected.getDoctor() + "?", ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Delete");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    Appointment.deleteAppointment(selected.getId());
                    loadAppointments();
                    statusLabel.setText("Appointment deleted.");
                }
            });
        }
    }

    @FXML
    private void clearForm() {
        doctorField.clear();
        datePicker.setValue(null);
        timeField.clear();
        reasonField.clear();
        statusLabel.setText("");
    }
}

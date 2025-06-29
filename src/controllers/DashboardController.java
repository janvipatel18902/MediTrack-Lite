package controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import models.Appointment;
import models.Medicine;

import java.time.LocalDate;
import java.util.List;

public class DashboardController {

    @FXML private ListView<String> todayMedicinesList;
    @FXML private ListView<String> todayAppointmentsList;
    @FXML private ToggleButton darkModeToggle;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        refreshDashboard();
    }

    @FXML
    private void refreshDashboard() {
        todayMedicinesList.getItems().clear();
        todayAppointmentsList.getItems().clear();

        LocalDate today = LocalDate.now();

        List<Medicine> meds = Medicine.getAllMedicines();
        for (Medicine m : meds) {
            LocalDate start = LocalDate.parse(m.getStartDate());
            LocalDate end = start.plusDays(m.getDuration());
            if ((today.isEqual(start) || today.isAfter(start)) && today.isBefore(end.plusDays(1))) {
                todayMedicinesList.getItems().add(m.getName() + " - " + m.getDosage());
            }
        }

        List<Appointment> apps = Appointment.getAppointmentsBetween(today, today.plusDays(7));
        for (Appointment a : apps) {
            todayAppointmentsList.getItems().add(a.getDoctor() + " - " + a.getDate() + " at " + a.getTime() + " - " + a.getReason());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MediTrack Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

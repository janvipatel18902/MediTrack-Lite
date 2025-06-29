package controllers;

import database.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Load all three tabs after successful login
                Parent dashboardPane = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
                Parent medicinePane = FXMLLoader.load(getClass().getResource("/fxml/medicine.fxml"));
                Parent appointmentPane = FXMLLoader.load(getClass().getResource("/fxml/appointment.fxml"));

                TabPane tabPane = new TabPane(
                    new Tab("Dashboard", dashboardPane),
                    new Tab("Medicines", medicinePane),
                    new Tab("Appointments", appointmentPane)
                );

                Scene scene = new Scene(tabPane, 400, 600);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setTitle("MediTrack Dashboard");
                stage.setScene(scene);
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        } catch (Exception e) {
            errorLabel.setText("Login failed: " + e.getMessage());
        }
    }

    @FXML
    private void openRegisterPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register - MediTrack");
        } catch (Exception e) {
            errorLabel.setText("Error loading register page.");
        }
    }
}

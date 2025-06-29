package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.AuthUtil;
import utils.SceneUtil;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;
    @FXML private Button registerButton; // Needed to get the current stage for scene switching

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
            return;
        }

        boolean success = AuthUtil.register(username, password);
        if (success) {
            statusLabel.setText("Registration successful. Redirecting...");
            SceneUtil.switchScene((Stage) registerButton.getScene().getWindow(), "/fxml/login.fxml", "Login");
        } else {
            statusLabel.setText("Username already exists.");
        }
    }

    @FXML
    private void openLoginPage() {
        SceneUtil.switchScene((Stage) registerButton.getScene().getWindow(), "/fxml/login.fxml", "Login");
    }
}

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Statement;
import database.DBConnection;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        createTablesIfNotExists();

        // Load the login page first
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        stage.setTitle("MediTrack Lite - Login");
        stage.setScene(new Scene(loginRoot));
        stage.show();
    }

    private void createTablesIfNotExists() {
        String sqlUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            );
        """;

        String sqlMedicines = """
            CREATE TABLE IF NOT EXISTS medicines (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                dosage TEXT NOT NULL,
                start_date TEXT NOT NULL,
                duration INTEGER NOT NULL
            );
        """;

        String sqlAppointments = """
            CREATE TABLE IF NOT EXISTS appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                doctor TEXT NOT NULL,
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                reason TEXT
            );
        """;

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsers);       // ✅ Create users table with username
            stmt.execute(sqlMedicines);   // ✅ Create medicines table
            stmt.execute(sqlAppointments);// ✅ Create appointments table
        } catch (Exception e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

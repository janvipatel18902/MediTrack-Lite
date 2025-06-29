package models;

import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Medicine {
    private int id;
    private String name;
    private String dosage;
    private String startDate;
    private int duration;

    public Medicine(int id, String name, String dosage, String startDate, int duration) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.startDate = startDate;
        this.duration = duration;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public String getStartDate() { return startDate; }
    public int getDuration() { return duration; }

    public void setName(String name) { this.name = name; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setDuration(int duration) { this.duration = duration; }

    public static List<Medicine> getAllMedicines() {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Medicine(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("dosage"),
                    rs.getString("start_date"),
                    rs.getInt("duration")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insert() {
        String sql = "INSERT INTO medicines(name, dosage, start_date, duration) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, dosage);
            stmt.setString(3, startDate);
            stmt.setInt(4, duration);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        String sql = "UPDATE medicines SET name=?, dosage=?, start_date=?, duration=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, dosage);
            stmt.setString(3, startDate);
            stmt.setInt(4, duration);
            stmt.setInt(5, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        String sql = "DELETE FROM medicines WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

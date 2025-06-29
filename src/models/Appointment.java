// Appointment.java
package models;

import database.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private int id;
    private String doctor;
    private String date;
    private String time;
    private String reason;

    public Appointment(int id, String doctor, String date, String time, String reason) {
        this.id = id;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.reason = reason;
    }

    public int getId() { return id; }
    public String getDoctor() { return doctor; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getReason() { return reason == null ? "" : reason; }

    public static List<Appointment> getAppointmentsForDate(LocalDate date) {
        return getAppointmentsBetween(date, date);
    }

    public static List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE date BETWEEN ? AND ? ORDER BY date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, start.toString());
            stmt.setString(2, end.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("id"),
                    rs.getString("doctor"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("reason")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("id"),
                    rs.getString("doctor"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("reason")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void saveAppointment(Appointment a) {
        String sql = "INSERT INTO appointments (doctor, date, time, reason) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.getDoctor());
            stmt.setString(2, a.getDate());
            stmt.setString(3, a.getTime());
            stmt.setString(4, a.getReason());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Medicine;

import java.sql.*;
import java.time.LocalDate;

public class MedicineController {

    @FXML private TextField nameField;
    @FXML private TextField dosageField;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField durationField;
    @FXML private TextField searchField;
    @FXML private ListView<String> medicineList;
    @FXML private Label statusLabel;

    private ObservableList<String> displayList = FXCollections.observableArrayList();
    private ObservableList<Medicine> fullList = FXCollections.observableArrayList();

    private Medicine selectedMedicine = null;

    @FXML
    public void initialize() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applySearchFilter());
        medicineList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int i = newVal.intValue();
            if (i >= 0 && i < fullList.size()) {
                selectedMedicine = fullList.get(i);
                nameField.setText(selectedMedicine.getName());
                dosageField.setText(selectedMedicine.getDosage());
                startDatePicker.setValue(LocalDate.parse(selectedMedicine.getStartDate()));
                durationField.setText(String.valueOf(selectedMedicine.getDuration()));
            }
        });
        loadMedicines();
    }

    private void loadMedicines() {
        fullList.setAll(Medicine.getAllMedicines());
        applySearchFilter();
    }

    private void applySearchFilter() {
        String keyword = searchField.getText().toLowerCase();
        displayList.clear();
        for (Medicine m : fullList) {
            if (m.getName().toLowerCase().contains(keyword) || m.getDosage().toLowerCase().contains(keyword)) {
                displayList.add(m.getName() + " - " + m.getDosage());
            }
        }
        medicineList.setItems(displayList);
    }

    @FXML
    private void addMedicine() {
        String name = nameField.getText();
        String dosage = dosageField.getText();
        LocalDate start = startDatePicker.getValue();
        String durationText = durationField.getText();

        if (name.isEmpty() || dosage.isEmpty() || start == null || durationText.isEmpty()) {
            statusLabel.setText("Fill all fields.");
            return;
        }

        try {
            int duration = Integer.parseInt(durationText);
            Medicine med = new Medicine(0, name, dosage, start.toString(), duration);
            med.insert();
            statusLabel.setText("Medicine added.");
            clearForm();
            loadMedicines();
        } catch (Exception e) {
            statusLabel.setText("Invalid duration.");
        }
    }

    @FXML
    private void editMedicine() {
        if (selectedMedicine == null) {
            statusLabel.setText("Select a medicine to edit.");
            return;
        }

        try {
            selectedMedicine.setName(nameField.getText());
            selectedMedicine.setDosage(dosageField.getText());
            selectedMedicine.setStartDate(startDatePicker.getValue().toString());
            selectedMedicine.setDuration(Integer.parseInt(durationField.getText()));
            selectedMedicine.update();
            statusLabel.setText("Medicine updated.");
            clearForm();
            loadMedicines();
        } catch (Exception e) {
            statusLabel.setText("Update failed.");
        }
    }

    @FXML
    private void deleteMedicine() {
        if (selectedMedicine == null) {
            statusLabel.setText("Select a medicine to delete.");
            return;
        }

        selectedMedicine.delete();
        statusLabel.setText("Medicine deleted.");
        clearForm();
        loadMedicines();
    }

    @FXML
    private void clearForm() {
        nameField.clear();
        dosageField.clear();
        startDatePicker.setValue(null);
        durationField.clear();
        searchField.clear();
        selectedMedicine = null;
        medicineList.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }
}

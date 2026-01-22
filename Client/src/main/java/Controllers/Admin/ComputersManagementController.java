package Controllers.Admin;

import POJO.Computer;
import ServerWORK.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ComputersManagementController {

    @FXML private TableView<Computer> computersTable;
    @FXML private TableColumn<Computer, Integer> idColumn;
    @FXML private TableColumn<Computer, String> specsColumn;
    @FXML private TableColumn<Computer, Boolean> statusColumn;
    @FXML private Label statusLabel;

    private ObservableList<Computer> computersData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Настройка столбцов таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("computerId"));
        specsColumn.setCellValueFactory(new PropertyValueFactory<>("specs"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("alive"));

        // Кастомный рендерер для статуса
        statusColumn.setCellFactory(col -> new TableCell<Computer, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Активен" : "Неактивен");
                    setStyle(item ?
                            "-fx-text-fill: #00e676; -fx-font-weight: bold;" :
                            "-fx-text-fill: #ff5252; -fx-font-weight: bold;");
                }
            }
        });

        // Настройка стиля таблицы
        computersTable.setStyle("-fx-control-inner-background: #1a1a1a; " +
                "-fx-text-fill: white; " +
                "-fx-selection-bar: #9c4dff; " +
                "-fx-selection-bar-non-focused: #9c4dff;");

        loadComputers();
    }

    private void loadComputers() {
        try {
            ConnectDB.user.sendMessage("get_all_computers");
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                computersData.setAll((List<Computer>) response);
                computersTable.setItems(computersData);
                statusLabel.setText("Загружено компьютеров: " + computersData.size());
            } else {
                statusLabel.setText("Ошибка загрузки данных");
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить компьютеры: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadComputers();
    }

    @FXML
    private void handleChangeStatus() {
        Computer selected = computersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите компьютер для изменения статуса");
            return;
        }

        boolean newStatus = !selected.getAlive();
        updateComputerStatus(selected.getComputerId(), newStatus);
    }

    private void updateComputerStatus(int computerId, boolean newStatus) {
        try {
            ConnectDB.user.sendMessage("update_computer_status");
            ConnectDB.user.sendObject(new Object[]{computerId, newStatus});

            String response = (String) ConnectDB.user.readObject();

            if ("OK".equals(response)) {
                statusLabel.setText("Статус компьютера успешно обновлен");
                loadComputers();
            } else {
                showAlert("Ошибка", response);
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось обновить статус: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuAdmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) computersTable.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить главное меню: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Стилизация Alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #1a1a1a; " +
                "-fx-text-fill: white; " +
                "-fx-border-color: #9c4dff; " +
                "-fx-border-width: 2;");

        alert.showAndWait();
    }
}
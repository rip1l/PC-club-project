package Controllers.Admin;

import POJO.Booking;
import ServerWORK.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.util.Callback;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class BookingManagementController {

    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, Integer> idColumn;
    @FXML private TableColumn<Booking, Integer> clientColumn;
    @FXML private TableColumn<Booking, Integer> computerColumn;
    @FXML private TableColumn<Booking, String> startColumn;
    @FXML private TableColumn<Booking, String> endColumn;
    @FXML private TableColumn<Booking, String> statusColumn;

    @FXML private Label statusLabel;

    private ObservableList<Booking> bookingsData = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final ObservableList<String> statusOptions =
            FXCollections.observableArrayList("pending", "confirmed", "completed", "cancelled");

    @FXML
    private void initialize() {
        // Настройка стиля таблицы
        bookingsTable.setStyle("-fx-control-inner-background: #1a1a1a; " +
                "-fx-text-fill: white; " +
                "-fx-selection-bar: #9c4dff; " +
                "-fx-selection-bar-non-focused: #9c4dff;");

        configureTableColumns();
        loadBookings();
    }

    private void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        computerColumn.setCellValueFactory(new PropertyValueFactory<>("computerId"));

        // Форматирование дат
        startColumn.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getStartTime();
            return new javafx.beans.property.SimpleStringProperty(
                    date != null ? date.format(formatter) : "");
        });

        endColumn.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getEndTime();
            return new javafx.beans.property.SimpleStringProperty(
                    date != null ? date.format(formatter) : "");
        });

        // Настройка ComboBox для статуса
        statusColumn.setCellFactory(column -> new TableCell<Booking, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>(statusOptions);

            {
                comboBox.setStyle("-fx-background-color: #2b2b2b; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-color: #9c4dff; " +
                        "-fx-border-width: 1; " +
                        "-fx-background-radius: 5; " +
                        "-fx-pref-width: 120;");

                comboBox.setOnAction(event -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    booking.setStatus(comboBox.getValue());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(item);
                    setGraphic(comboBox);
                }
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadBookings() {
        try {
            ConnectDB.user.sendMessage("get_all_bookings");
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                bookingsData.setAll((List<Booking>) response);
                bookingsTable.setItems(bookingsData);
                statusLabel.setText("Загружено: " + bookingsData.size() + " бронирований");
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить бронирования: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveChanges() {
        try {
            ConnectDB.user.sendMessage("update_bookings_status");
            ConnectDB.user.sendObject(new ArrayList<>(bookingsTable.getItems()));

            String response = (String) ConnectDB.user.readObject();
            if ("OK".equals(response)) {
                showAlert("Успех", "Статусы успешно обновлены");
                loadBookings();
            } else {
                showAlert("Ошибка", response);
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка при сохранении: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadBookings();
    }

    @FXML
    private void handleFilterActive() {
        ObservableList<Booking> filtered = bookingsData.filtered(b ->
                "confirmed".equals(b.getStatus()));
        bookingsTable.setItems(filtered);
        statusLabel.setText("Отфильтровано: " + filtered.size() + " активных бронирований");
    }

    @FXML
    private void handleFilterAll() {
        bookingsTable.setItems(bookingsData);
        statusLabel.setText("Загружено: " + bookingsData.size() + " бронирований");
    }

    @FXML
    private void handleCancelBooking() {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите бронирование для отмены");
            return;
        }

        selected.setStatus("cancelled");
        statusLabel.setText("Бронирование #" + selected.getBookingId() + " помечено как отмененное");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuAdmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) bookingsTable.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить меню: " + e.getMessage());
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
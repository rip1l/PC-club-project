package Controllers.Admin;

import POJO.ClientGroup;
import ServerWORK.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GroupManagementController {

    @FXML private TableView<ClientGroup> groupsTable;
    @FXML private TableColumn<ClientGroup, Integer> idColumn;
    @FXML private TableColumn<ClientGroup, String> nameColumn;
    @FXML private TableColumn<ClientGroup, Double> discountColumn;

    @FXML private Label statusLabel;
    @FXML private GridPane clientForm;
    @FXML private TextField clientIdField;
    @FXML private TextField groupIdField;

    private ObservableList<ClientGroup> groupsData = FXCollections.observableArrayList();
    private boolean isAddOperation = true;

    @FXML
    private void initialize() {
        // Настройка стиля таблицы
        groupsTable.setStyle("-fx-control-inner-background: #1a1a1a; " +
                "-fx-text-fill: white; " +
                "-fx-selection-bar: #9c4dff; " +
                "-fx-selection-bar-non-focused: #9c4dff;");

        configureTableColumns();
        loadGroups();
    }

    private void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("groupId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discountPersent"));

        // Форматирование скидки
        discountColumn.setCellFactory(column -> new TableCell<ClientGroup, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", item));
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    private void loadGroups() {
        try {
            ConnectDB.user.sendMessage("get_all_groups");
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                List<?> responseList = (List<?>) response;
                if (!responseList.isEmpty() && responseList.get(0) instanceof ClientGroup) {
                    groupsData.setAll((List<ClientGroup>) response);
                    groupsTable.setItems(groupsData);
                    statusLabel.setText("Загружено групп: " + groupsData.size());
                } else {
                    statusLabel.setText("Нет данных о группах");
                }
            } else {
                statusLabel.setText("Неверный формат ответа от сервера");
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить группы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddClient() {
        isAddOperation = true;
        clientForm.setVisible(true);
        statusLabel.setText("Введите данные для добавления клиента");
    }

    @FXML
    private void handleRemoveClient() {
        isAddOperation = false;
        clientForm.setVisible(true);
        statusLabel.setText("Введите данные для удаления клиента");
    }

    @FXML
    private void handleConfirm() {
        try {
            int clientId = Integer.parseInt(clientIdField.getText());
            int groupId = Integer.parseInt(groupIdField.getText());

            String operation = isAddOperation ? "add_client_to_group" : "remove_client_from_group";
            String operationName = isAddOperation ? "Добавление клиента в группу" : "Удаление клиента из группы";

            ConnectDB.user.sendMessage(operation);
            ConnectDB.user.sendObject(new Object[]{clientId, groupId});

            String response = (String) ConnectDB.user.readObject();
            if ("OK".equals(response)) {
                statusLabel.setText(operationName + " успешно выполнено");
                clientForm.setVisible(false);
                clearForm();
            } else {
                showAlert("Ошибка", response);
            }
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные числовые ID");
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка при выполнении операции: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        clientForm.setVisible(false);
        clearForm();
        statusLabel.setText("Операция отменена");
    }

    private void clearForm() {
        clientIdField.clear();
        groupIdField.clear();
    }

    @FXML
    private void handleRefresh() {
        loadGroups();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuAdmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) groupsTable.getScene().getWindow();
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
package Controllers.Admin;

import POJO.Users;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WorkUsersAdminController {

    @FXML private VBox mainContainer;
    @FXML private TextField loginToDeleteField;
    @FXML private TextField loginToUpdateField;
    @FXML private TextField newRoleField;
    @FXML private TextField loginTosearchField1;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private ListView<String> usersListView;

    private List<Users> usersList = new ArrayList<>();

    @FXML
    private void initialize() {
        // Настройка стиля ListView
        usersListView.setStyle("-fx-control-inner-background: #1a1a1a; " +
                "-fx-text-fill: white; " +
                "-fx-selection-bar: #9c4dff; " +
                "-fx-selection-bar-non-focused: #9c4dff;");
    }

    @FXML
    private void deleteUser() {
        String login = loginToDeleteField.getText().trim();
        if (login.isEmpty()) {
            showAlert("Ошибка", "Введите логин пользователя.");
            return;
        }

        try {
            ConnectDB.user.sendMessage("deleteUser");
            ConnectDB.user.sendObject(login);

            String response = (String) ConnectDB.user.readObject();
            if ("User deleted successfully".equals(response)) {
                showAlert("Успех", "Пользователь удален.");
                viewAllUsers();
            } else {
                showAlert("Ошибка", "Не удалось удалить пользователя.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при удалении пользователя.");
        }
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = loginTosearchField1.getText().trim();
        List<Users> filteredUsers = usersList.stream()
                .filter(user -> user.getLogin().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        updateUsersListView(filteredUsers);
    }

    @FXML
    private void handleSort() {
        String selectedSortOrder = sortComboBox.getValue();
        List<Users> sortedUsers = new ArrayList<>(usersList);

        if ("A-Z".equals(selectedSortOrder)) {
            sortedUsers.sort(Comparator.comparing(Users::getLogin));
        } else if ("Z-A".equals(selectedSortOrder)) {
            sortedUsers.sort((u1, u2) -> u2.getLogin().compareTo(u1.getLogin()));
        }

        updateUsersListView(sortedUsers);
    }

    @FXML
    private void handleFilter() {
        String selectedRole = roleComboBox.getValue();
        List<Users> filteredUsers = usersList.stream()
                .filter(user -> user.getRole().equalsIgnoreCase(selectedRole))
                .collect(Collectors.toList());
        updateUsersListView(filteredUsers);
    }

    @FXML
    private void updateUserRole() {
        String login = loginToUpdateField.getText().trim();
        String newRole = newRoleField.getText().trim();

        if (login.isEmpty() || newRole.isEmpty()) {
            showAlert("Ошибка", "Введите логин и новую роль пользователя.");
            return;
        }

        try {
            ConnectDB.user.sendMessage("updateUserRole");
            ConnectDB.user.sendObject(login);
            ConnectDB.user.sendObject(newRole);

            String response = (String) ConnectDB.user.readObject();
            if ("User role updated successfully".equals(response)) {
                showAlert("Успех", "Роль пользователя обновлена.");
                viewAllUsers();
            } else {
                showAlert("Ошибка", "Не удалось обновить роль пользователя.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при изменении роли пользователя.");
        }
    }

    @FXML
    private void viewAllUsers() {
        try {
            ConnectDB.user.sendMessage("viewAllUsers");
            usersList = (List<Users>) ConnectDB.user.readObject();
            updateUsersListView(usersList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Произошла ошибка при получении списка пользователей.");
        }
    }

    private void updateUsersListView(List<Users> users) {
        usersListView.getItems().clear();
        for (Users user : users) {
            usersListView.getItems().add(String.format("Логин: %-20s Роль: %s", user.getLogin(), user.getRole()));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Стилизация Alert
        alert.getDialogPane().setStyle("-fx-background-color: #1a1a1a; " +
                "-fx-text-fill: white; " +
                "-fx-border-color: #9c4dff; " +
                "-fx-border-width: 2;");
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuAdmin.fxml"));
            Parent registerPage = loader.load();
            Scene registerScene = new Scene(registerPage);

            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setWidth(900);
            stage.setHeight(700);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
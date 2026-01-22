package Controllers.Сlient;

import POJO.Clients;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AddInfoClientController {

    @FXML
    private VBox mainContainer;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField middleNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Button saveButton;

    @FXML
    void saveClientInfo() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String middleName = middleNameField.getText();
        String phoneNumber = phoneNumberField.getText();

        // Проверка на пустые поля
        if (lastName.isEmpty() || firstName.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Ошибка", "Все обязательные поля должны быть заполнены.");
            return;
        }

        // Получение id текущего пользователя из ConnectDB
        int userId = ConnectDB.id;  // Используем id из ConnectDB

        if (userId == 0) {
            showAlert("Ошибка", "Не удалось получить данные пользователя.");
            return;
        }

        // Создание объекта Clients
        Clients client = new Clients();
        client.setUserId(userId);  // Устанавливаем userId
        client.setLastName(lastName);
        client.setFirstName(firstName);
        client.setMiddleName(middleName);
        client.setPhoneNumber(phoneNumber);

        // Отправка данных на сервер
        ConnectDB.user.sendMessage("addClientInfo");
        ConnectDB.user.sendObject(client);
        System.out.println("Информация о клиенте отправлена");

        // Получение ответа от сервера
        String response = "";
        try {
            response = ConnectDB.user.readMessage();
        } catch (IOException ex) {
            System.out.println("Ошибка при чтении сообщения");
        }

        // Обработка ответа от сервера
        if (response.equals("Client information already exists")) {
            showAlert("Ошибка", "Информация уже существует.");
        } else if (response.equals("Client information added successfully")) {
            showAlert("Успех", "Информация добавлена.");
            // Переход к следующему экрану или выполнение других действий
        } else {
            showAlert("Ошибка", "Произошла ошибка при добавлении информации.");
        }
    }

    // Метод для показа сообщений
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBack()
    {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/infoClient.fxml"));
            Parent registerPage = loader.load();
            Scene registerScene = new Scene(registerPage);

            // Получаем текущее окно и устанавливаем новую сцену
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final String HOVER_STYLE = "-fx-background-radius: 15; " +
            "-fx-border-radius: 15; " +
            "-fx-font-family: 'Georgia'; " +
            "-fx-font-size: 16; " +
            "-fx-pref-width: 130; " +
            "-fx-pref-height: 40; " +
            "-fx-background-color: #333333; " + // Тёмный фон
            "-fx-text-fill: #FFF8DC; " +       // Светлый текст
            "-fx-border-color: black; " +
            "-fx-border-width: 2;";

    // Исходный стиль
    private static final String DEFAULT_STYLE = "-fx-background-radius: 15; " +
            "-fx-border-radius: 15; " +
            "-fx-font-family: 'Georgia'; " +
            "-fx-font-size: 16; " +
            "-fx-pref-width: 130; " +
            "-fx-pref-height: 40; " +
            "-fx-background-color: white; " + // Прозрачный фон
            "-fx-text-fill: black; " +              // Чёрный текст
            "-fx-border-color: black; " +
            "-fx-border-width: 2;";

    @FXML
    private void handleMouseEnterExit(javafx.scene.input.MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(HOVER_STYLE); // Устанавливаем стиль при наведении
    }

    @FXML
    private void handleMouseExitExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(DEFAULT_STYLE); // Возвращаем исходный стиль
    }
}

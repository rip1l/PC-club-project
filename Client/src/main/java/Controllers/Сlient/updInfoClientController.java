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
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class updInfoClientController {

    // Объявляем поля для доступа к элементам FXML

    @FXML
    private VBox mainContainer;
    @FXML
    private TextField loginField1;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField middleNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Button updButton;
    @FXML
    private Button backButton;

    // Метод, который будет вызываться при нажатии на кнопку "Изменить"
    @FXML
    private void handleUpdateInfo(ActionEvent event) {
        // Получаем данные из полей
        String newLogin = loginField1.getText();
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String middleName = middleNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        Clients client = new Clients();
        int userId = ConnectDB.id;
        client.setUserId(userId);  // Устанавливаем userId
        client.setLastName(lastName);
        client.setFirstName(firstName);
        client.setMiddleName(middleName);
        client.setPhoneNumber(phoneNumber);
        ConnectDB.user.sendMessage("updateClientInfo");
        ConnectDB.user.sendObject(newLogin);  // Отправляем логин
        ConnectDB.user.sendObject(client);
        String response = (String) ConnectDB.user.readObject();
        showAlert("Результат обновления", response);
        // Здесь необходимо добавить логику для отправки данных на сервер
        // Например, отправить данные через сетевое соединение (клиент-сервер)
        // Для примера просто показываем информационное сообщение
        showAlert("Информация", "Данные успешно обновлены!");

        // Очистка полей после обновления
        loginField1.clear();
        lastNameField.clear();
        firstNameField.clear();
        middleNameField.clear();
        phoneNumberField.clear();
    }

    // Метод, который будет вызываться при нажатии на кнопку "Назад"
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
    // Метод для отображения сообщений
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

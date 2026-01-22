package Controllers.Сlient;

import POJO.ClientInfo;
import POJO.Clients;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class InfoClientController {

    @FXML
    private Label loginLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label middleNameLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private VBox mainContainer;
    @FXML
    private Label InstaLabel;
    @FXML
    private Label VkLabel;
    @FXML
    private Label mapLabel;
    @FXML
    private Label BalanceLabel;

    // Метод для получения информации о клиенте
    @FXML
    void initialize() {
        mapLabel.setStyle("-fx-text-fill: white; -fx-underline: false;");
        mapLabel.setOnMouseClicked(this::handleMapClick);
        VkLabel.setStyle("-fx-text-fill: white; -fx-underline: false;");
        VkLabel.setOnMouseClicked(this::handleVkClick);
        InstaLabel.setStyle("-fx-text-fill: white; -fx-underline: false;");
        InstaLabel.setOnMouseClicked(this::handleInstaClick);
        // Получение данных клиента по userId
        int userId = ConnectDB.id;  // Получаем ID текущего пользователя из ConnectDB
        if (userId == 0) {
            showAlert("Ошибка", "Не удалось получить данные пользователя.");
            return;
        }

        // Отправляем запрос на сервер для получения информации о клиенте
        ConnectDB.user.sendMessage("viewClientInfoForShow");  // Отправляем команду на сервер
        ConnectDB.user.sendObject(userId);  // Отправляем ID пользователя

        // Получаем объект клиента с сервера
        ClientInfo clientInfo = (ClientInfo) ConnectDB.user.readObject();

        if (clientInfo != null) {
            Clients client = clientInfo.getClient();
            double balance = clientInfo.getBalance();

            loginLabel.setText(ConnectDB.login);
            lastNameLabel.setText(client.getLastName());
            firstNameLabel.setText(client.getFirstName());
            middleNameLabel.setText(client.getMiddleName() != null ? client.getMiddleName() : "-");
            phoneNumberLabel.setText(client.getPhoneNumber());
            BalanceLabel.setText(String.format("%.2f", balance));
        } else {
            showAlert("Ошибка", "Информация о клиенте не найдена.");
        }
    }
    @FXML //Открытие карты по нажатию на ссылку
    private void handleMapClick(MouseEvent event) {
        String url = "https://yandex.by/maps/157/minsk/search/%D0%9A%D0%BE%D0%BC%D0%BF%D1%8C%D1%8E%D1%82%D0%B5%D1%80%D0%BD%D1%8B%D0%B5%20%D0%BA%D0%BB%D1%83%D0%B1%D1%8B/?ll=27.564453%2C53.893008&sll=27.579756%2C53.899853&sspn=0.347149%2C0.124823&z=11.84";
        try {
            Desktop.getDesktop().browse(new URI(url));}
        catch (IOException | URISyntaxException e) {e.printStackTrace();}
    }

    @FXML //Открытие карты по нажатию на ссылку
    private void handleVkClick(MouseEvent event) {
        String url = "https://vk.com/metaarenaminsk?ysclid=mavi60zs3l97933500";
        try {
            Desktop.getDesktop().browse(new URI(url));}
        catch (IOException | URISyntaxException e) {e.printStackTrace();}
    }

    @FXML //Открытие карты по нажатию на ссылку
    private void handleInstaClick(MouseEvent event) {
        String url = "https://www.instagram.com/metaarenaminsk/";
        try {
            Desktop.getDesktop().browse(new URI(url));}
        catch (IOException | URISyntaxException e) {e.printStackTrace();}
    }
    @FXML
    private void handleAddInfo()
    {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddInfoClient.fxml"));
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
    @FXML
    private void handleUpdInfo()
    {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updInfoClient.fxml"));
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
    @FXML
    private void handleBack()
    {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/windowClient.fxml"));
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
    // Метод для показа сообщений
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

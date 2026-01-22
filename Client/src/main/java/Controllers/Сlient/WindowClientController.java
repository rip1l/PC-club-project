package Controllers.Сlient;

import Controllers.SceneLoader;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowClientController {

    @FXML
    private VBox mainContainer;

    @FXML
    public void handleClientInfo() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/infoClient.fxml", 800, 600);
    }
    @FXML
    public void handleTariff() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/viewTariffCL.fxml", 800, 600);
    }
    @FXML
    public void handleInfoClub() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/ViewInfo.fxml", 1000, 600);
    }
    @FXML
    public void handleShowAllPc() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/showAllPc.fxml", 1000, 600);
    }
    @FXML
    public void handleReviews() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/reviewCl.fxml", 800, 600);
    }
    public void handleAddBalance() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/addBalanceCL.fxml", 800, 600);
    }
    @FXML
    public void handleBookingPc() {
        // Отправляем запрос на сервер для проверки клиента
        ConnectDB.user.sendMessage("checkClientExists");
        ConnectDB.user.sendObject(ConnectDB.id); // Используем ID, хранящийся в ConnectDB

        String response = "";
        try {
            response = ConnectDB.user.readMessage(); // Читаем ответ от сервера
        } catch (IOException ex) {
            System.out.println("Ошибка при чтении ответа от сервера");
        }

        // Обработка ответа
        if (response.equals("Client exists")) {
            // Если клиент найден, переходим на сцену Order
            SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/bookingPc.fxml", 800, 600);
        } else if (response.equals("Client does not exist")) {
            // Если клиента нет, показываем сообщение об ошибке
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Клиент не найден");
            alert.setContentText("Пожалуйста, проверьте данные клиента.");
            alert.showAndWait();
        } else {
            // Обработка неожиданного ответа
            System.out.println("Неожиданный ответ: " + response);
        }
    }

    @FXML
    private void handleBack()
    {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
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
}

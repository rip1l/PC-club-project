package Controllers.Сlient;

import POJO.Computer;
import ServerWORK.ConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ViewComputersController {
    @FXML
    private Button backButton;
    @FXML
    private VBox computerTemplate;
    @FXML
    private VBox computersContainer;
    @FXML
    private VBox mainContainer;

    private List<Computer> computers;

    @FXML
    public void initialize() {
        loadComputers();
    }

    private void loadComputers() {
        try {
            ConnectDB.user.sendMessage("get_all_computers");

            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                computers = (List<Computer>) response;
                displayComputers(computers);
            } else {
                System.out.println("Ошибка при получении компьютеров: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayComputers(List<Computer> computers) {
        computersContainer.getChildren().clear(); // Очищаем контейнер перед добавлением

        for (Computer computer : computers) {
            // Создаем копию шаблона для каждого компьютера
            VBox computerBox = new VBox();
            computerBox.getStyleClass().addAll(computerTemplate.getStyleClass());
            computerBox.setStyle(computerTemplate.getStyle());
            computerBox.setVisible(true);

            // Создаем элементы для отображения данных
            HBox contentBox = new HBox();
            contentBox.setSpacing(20);
            contentBox.setAlignment(Pos.CENTER_LEFT);

            // Левая часть (номер и статус)
            VBox leftBox = new VBox();
            leftBox.setSpacing(5);
            leftBox.setMinWidth(150);
            leftBox.setAlignment(Pos.CENTER_LEFT);

            Label numberLabel = new Label("ПК №" + computer.getComputerId());
            numberLabel.setStyle("-fx-text-fill: #9c4dff; -fx-font-size: 20px; -fx-font-weight: bold;");

            Label statusLabel = new Label();
            if (computer.getAlive()) {
                statusLabel.setText("Доступен");
                statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 14px;"); // Зеленый цвет для доступного
            } else {
                statusLabel.setText("Недоступен");
                statusLabel.setStyle("-fx-text-fill: #F44336; -fx-font-size: 14px;"); // Красный цвет для недоступного
            }

            leftBox.getChildren().addAll(numberLabel, statusLabel);

            // Правая часть (характеристики)
            VBox rightBox = new VBox();
            rightBox.setSpacing(5);
            rightBox.setMinWidth(700);
            rightBox.setAlignment(Pos.CENTER_LEFT);

            Label specsTitle = new Label("Характеристики:");
            specsTitle.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 14px;");

            Label specsLabel = new Label(computer.getSpecs());
            specsLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 14px;");
            specsLabel.setWrapText(true);

            rightBox.getChildren().addAll(specsTitle, specsLabel);

            // Собираем всё вместе
            contentBox.getChildren().addAll(leftBox, rightBox);
            computerBox.getChildren().add(contentBox);

            // Добавляем в контейнер
            computersContainer.getChildren().add(computerBox);
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/windowClient.fxml"));
            Parent registerPage = loader.load();
            Scene registerScene = new Scene(registerPage);

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
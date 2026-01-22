package Controllers.Admin;

import POJO.Services;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AddServiceController {

    @FXML
    private TextField serviceName;
    @FXML
    private TextArea serviceDescription;
    @FXML
    private TextField servicePrice;
    @FXML
    private ComboBox<Integer> durationValue;
    @FXML
    private ComboBox<String> durationUnit;
    @FXML
    private Button addServiceButton;
    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        // Настройка кнопки добавления услуги
        addServiceButton.setOnAction(event -> addService());

        // Настройка кнопки "Назад"
        backButton.setOnAction(event -> handleBack());

        // Заполнение ComboBox'ов
        fillDurationComboBoxes();
    }

    // Метод для добавления услуги
    @FXML
    void addService() {
        String name = serviceName.getText();
        String description = serviceDescription.getText();
        String priceText = servicePrice.getText();
        Integer duration = durationValue.getValue();
        String unit = durationUnit.getValue();

        // Проверка на пустые поля
        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || duration == null || unit == null) {
            showAlertWithEmptyFields();
            return;
        }

        try {
            double price = Double.parseDouble(priceText); // Преобразуем цену в double

            // Создаем объект услуги
            Services service = new Services();
            service.setName(name);
            service.setDescription(description);
            service.setPrice(price);
            service.setDuration(duration + " " + unit);  // Преобразуем длительность в строку

            // Отправляем услугу на сервер
            sendServiceToServer(service);
        } catch (NumberFormatException e) {
            showAlertWithInvalidPrice();
        }
    }

    // Метод для отправки услуги на сервер
    private void sendServiceToServer(Services service) {
        try {
            ConnectDB.user.sendMessage("addService");
            ConnectDB.user.sendObject(service);
            System.out.println("Услуга отправлена на сервер");

            String mes = ConnectDB.user.readMessage();
            if (mes.equals("Service added successfully")) {
                showAlertWithSuccess();
            } else {
                showAlertWithError();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlertWithServerError();
        }
    }


    // Заполнение ComboBox для длительности услуги
    private void fillDurationComboBoxes() {
        // Заполнение значений для durationValue (например, 1-60 минут)
        for (int i = 1; i <= 60; i++) {
            durationValue.getItems().add(i);
        }

        // Заполнение значений для durationUnit (например, "минуты", "часы")
        durationUnit.getItems().addAll("минуты", "часы");
    }

    // Показать ошибку при пустых полях
    static public void showAlertWithEmptyFields() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Пустые поля");
        alert.setContentText("Пожалуйста, заполните все поля.");
        alert.showAndWait();
    }

    // Показать ошибку при неправильной цене
    static public void showAlertWithInvalidPrice() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Неверная цена");
        alert.setContentText("Пожалуйста, введите корректную цену.");
        alert.showAndWait();
    }

    // Показать успешное сообщение
    static public void showAlertWithSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText("Услуга добавлена");
        alert.setContentText("Услуга была успешно добавлена.");
        alert.showAndWait();
    }

    // Показать ошибку при добавлении услуги
    static public void showAlertWithError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Добавление услуги");
        alert.setContentText("Произошла ошибка при добавлении услуги. Попробуйте позже.");
        alert.showAndWait();
    }

    // Показать ошибку сервера
    static public void showAlertWithServerError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка сервера");
        alert.setContentText("Не удалось подключиться к серверу. Попробуйте позже.");
        alert.showAndWait();
    }

    // Обработчик кнопки "Назад"
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/serviceAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Админ панель");
            stage.setScene(new Scene(root));
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();

            // Закрытие текущего окна добавления услуги
            ((Stage) backButton.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Стиль при наведении
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
    private void handleMouseEnterExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(HOVER_STYLE); // Устанавливаем стиль при наведении
    }

    @FXML
    private void handleMouseExitExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(DEFAULT_STYLE); // Возвращаем исходный стиль
    }
    @FXML
    private void handleMouseEnter(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-font-family: 'Georgia'; -fx-font-size: 20; -fx-pref-width: 200; -fx-pref-height: 50; -fx-background-color: #333333; -fx-text-fill: #FAFAFA;"); // Темный фон при наведении
    }

    @FXML
    private void handleMouseExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-font-family: 'Georgia'; -fx-font-size: 20;  -fx-pref-width: 200; -fx-pref-height: 50; -fx-background-color: black; -fx-text-fill: #FAFAFA;"); // Возврат к исходному черному фону
    }
}

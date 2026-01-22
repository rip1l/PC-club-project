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

import java.util.List;

public class UpdateServiceController {

    @FXML
    private ComboBox<Services> serviceComboBox;
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
    private Button updateServiceButton;
    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        // Загрузка списка услуг
        loadServicesList();

        // Настройка кнопки обновления услуги
        updateServiceButton.setOnAction(event -> updateService());

        // Настройка кнопки "Назад"
        backButton.setOnAction(event -> handleBack());

        // Заполнение ComboBox для длительности
        fillDurationComboBoxes();
    }

    // Загрузка списка услуг в ComboBox
    // Загрузка списка услуг в ComboBox
    private void loadServicesList() {
        try {
            ConnectDB.user.sendMessage("viewServices");
            List<Services> servicesList = (List<Services>) ConnectDB.user.readObject();
            serviceComboBox.getItems().setAll(servicesList);

            // Использование кастомной ячейки для отображения
            serviceComboBox.setCellFactory(param -> new ListCell<Services>() {
                @Override
                protected void updateItem(Services item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Форматируем вывод как: 3, Кератин, Прямые волосы, 500, 3 часа
                        setText(item.getServiseId() + ", " + item.getName() + ", " + item.getDescription() + ", " + item.getPrice() + ", " + item.getDuration());
                    }
                }
            });

            // Устанавливаем текст для отображаемого элемента
            serviceComboBox.setButtonCell(new ListCell<Services>() {
                @Override
                protected void updateItem(Services item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Форматируем вывод для выбранной услуги
                        setText(item.getServiseId() + ", " + item.getName() + ", " + item.getDescription() + ", " + item.getPrice() + ", " + item.getDuration());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlertWithError();
        }
    }

    // Метод для обновления услуги
    @FXML
    void updateService() {
        Services selectedService = serviceComboBox.getValue();
        if (selectedService == null) {
            showAlertWithEmptyFields();  // Услуга не выбрана
            return;
        }

        String name = serviceName.getText();
        String description = serviceDescription.getText();
        String priceText = servicePrice.getText();
        Integer duration = durationValue.getValue();
        String unit = durationUnit.getValue();

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || duration == null || unit == null) {
            showAlertWithEmptyFields();
            return;
        }

        try {
            double price = Double.parseDouble(priceText);  // Преобразуем цену

            selectedService.setName(name);
            selectedService.setDescription(description);
            selectedService.setPrice(price);
            selectedService.setDuration(duration + " " + unit);  // Длительность

            // Отправка обновленной услуги на сервер
            sendServiceToServer(selectedService);
        } catch (NumberFormatException e) {
            showAlertWithInvalidPrice();
        }
    }

    // Метод для отправки измененной услуги на сервер
    private void sendServiceToServer(Services service) {
        try {
            ConnectDB.user.sendMessage("updateService");  // Сообщаем серверу
            ConnectDB.user.sendObject(service);  // Отправляем услугу
            String response = ConnectDB.user.readMessage();
            if (response.equals("Service updated successfully")) {
                showAlertWithSuccess();
            } else {
                showAlertWithError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlertWithServerError();
        }
    }

    // Показать ошибку при пустых полях
    static void showAlertWithEmptyFields() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Пустые поля");
        alert.setContentText("Пожалуйста, заполните все поля.");
        alert.showAndWait();
    }

    // Показать ошибку при неправильной цене
    static void showAlertWithInvalidPrice() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Неверная цена");
        alert.setContentText("Пожалуйста, введите корректную цену.");
        alert.showAndWait();
    }

    // Показать успешное сообщение
    static void showAlertWithSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText("Услуга обновлена");
        alert.setContentText("Услуга была успешно обновлена.");
        alert.showAndWait();
    }

    // Показать ошибку при добавлении услуги
    static void showAlertWithError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Обновление услуги");
        alert.setContentText("Произошла ошибка при обновлении услуги. Попробуйте позже.");
        alert.showAndWait();
    }

    // Показать ошибку сервера
    static void showAlertWithServerError() {
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
            "-fx-background-color: white; " +
            "-fx-text-fill: black; " +
            "-fx-border-color: black; " +
            "-fx-border-width: 2;";

    @FXML
    private void handleMouseEnterExit(javafx.scene.input.MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(HOVER_STYLE);
    }

    @FXML
    private void handleMouseExitExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(DEFAULT_STYLE);
    }
    private void fillDurationComboBoxes() {
        for (int i = 1; i <= 60; i++) {
            durationValue.getItems().add(i);
        }
        durationUnit.getItems().addAll("минуты", "часы");
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

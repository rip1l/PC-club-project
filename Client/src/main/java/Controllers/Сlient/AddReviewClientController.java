package Controllers.Сlient;

import POJO.Review;  // Импортируем класс Review
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AddReviewClientController {

    @FXML
    private TextField ratingField;  // Поле для ввода рейтинга (1–5)
    @FXML
    private TextArea reviewTextArea; // Поле для ввода текста отзыва
    @FXML
    private Button saveReviewButton; // Кнопка для сохранения отзыва

    @FXML
    void saveReview() {
        String reviewText = reviewTextArea.getText();
        String ratingStr = ratingField.getText();

        // Проверка на пустые поля
        if (reviewText.isEmpty() || ratingStr.isEmpty()) {
            showAlert("Ошибка", "Все обязательные поля должны быть заполнены.");
            return;
        }

        // Проверка корректности рейтинга
        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) {
                showAlert("Ошибка", "Рейтинг должен быть от 1 до 5.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Неверный формат рейтинга.");
            return;
        }

        // Получение id текущего пользователя из ConnectDB
        int userId = ConnectDB.id;  // Используем id из ConnectDB

        if (userId == 0) {
            showAlert("Ошибка", "Не удалось получить данные пользователя.");
            return;
        }

        // Создание объекта Review
        Review review = new Review();
        review.setUsersId(userId);  // Устанавливаем usersId
        review.setReviewText(reviewText);
        review.setRating(rating);

        // Отправка данных на сервер
        ConnectDB.user.sendMessage("addReview");
        ConnectDB.user.sendObject(review);
        System.out.println("Отзыв отправлен");

        // Получение ответа от сервера
        String response = "";
        try {
            response = ConnectDB.user.readMessage();
        } catch (IOException ex) {
            System.out.println("Ошибка при чтении сообщения");
        }

        // Обработка ответа от сервера
        if (response.equals("Review already exists")) {
            showAlert("Ошибка", "Отзыв уже существует.");
        } else if (response.equals("Review added successfully")) {
            showAlert("Успех", "Отзыв добавлен.");
            // Переход к следующему экрану или выполнение других действий
        } else {
            showAlert("Ошибка", "Произошла ошибка при добавлении отзыва.");
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
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reviewCL.fxml"));
            Parent registerPage = loader.load();
            Scene registerScene = new Scene(registerPage);

            // Получаем текущее окно и устанавливаем новую сцену
            Stage stage = (Stage) ratingField.getScene().getWindow();
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
    private void handleMouseEnterExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(HOVER_STYLE); // Устанавливаем стиль при наведении
    }

    @FXML
    private void handleMouseExitExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(DEFAULT_STYLE); // Возвращаем исходный стиль
    }
}

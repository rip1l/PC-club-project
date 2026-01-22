package Controllers.Сlient;

import POJO.Review;
import POJO.Tariff;
import ServerWORK.ConnectDB;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ReviewsCLController {

    @FXML
    private VBox mainContainer;
    @FXML
    private VBox reviewsListContainer;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private Button sortByRatingButton;

    private List<Review> reviews;

    // Метод для загрузки всех отзывов с сервера
    private void loadReviews() {
        // Отправляем команду на сервер для получения всех отзывов
        ConnectDB.user.sendMessage("viewReviews");
        Object response = ConnectDB.user.readObject();
        if(response instanceof List<?>){
            reviews = (List<Review>) response;

            if(reviews !=null && !reviews.isEmpty()){
                Platform.runLater(() -> {
                    reviewsListContainer.getChildren().clear();
                    for (Review review : reviews) {
                        HBox tariffBox = createReviewBox(review);
                        reviewsListContainer.getChildren().add(tariffBox);
                    }
                });
                return;
            }
        }
    }

    private HBox createReviewBox(Review review) {
        HBox reviewBox = new HBox(20); // Увеличиваем расстояние между текстом и звёздами
        reviewBox.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #9c4dff; -fx-border-width: 1; -fx-border-radius: 10;");
        reviewBox.setPrefWidth(reviewsListContainer.getWidth() - 30); // Ширина по родителю
        reviewBox.setMaxWidth(Double.MAX_VALUE); // Растягиваем на всю ширину

        // Текст отзыва
        Label reviewText = new Label(review.getReviewText());
        reviewText.setFont(new Font(14));
        reviewText.setStyle("-fx-text-fill: white; -fx-wrap-text: true;"); // Белый текст с переносом
        reviewText.setMaxWidth(Double.MAX_VALUE); // Растягиваем текст
        HBox.setHgrow(reviewText, Priority.ALWAYS); // Занимаем всё доступное пространство

        // Оценка в виде звезд
        HBox ratingBox = new HBox(5);
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView("/images/star" + (i <= review.getRating() ? "filled" : "empty") + ".png");
            star.setFitWidth(20);
            star.setFitHeight(20);
            ratingBox.getChildren().add(star);
        }

        reviewBox.getChildren().addAll(reviewText, ratingBox);
        return reviewBox;
    }

    // Метод для обработки нажатия кнопки сортировки
    @FXML
    private void handleSortButtonClick() {
        String selectedSort = sortComboBox.getValue();

        if (reviews != null) {
            if ("От 1 до 5".equals(selectedSort)) {
                reviews = reviews.stream()
                        .sorted(Comparator.comparingInt(Review::getRating))  // Сортировка по возрастанию
                        .collect(Collectors.toList());
            } else if ("От 5 до 1".equals(selectedSort)) {
                reviews = reviews.stream()
                        .sorted((r1, r2) -> Integer.compare(r2.getRating(), r1.getRating()))  // Сортировка по убыванию
                        .collect(Collectors.toList());
            }

            // Очистка и повторная загрузка отзывов
            reviewsListContainer.getChildren().clear();
            for (Review review : reviews) {
                HBox reviewBox = createReviewBox(review);
                reviewsListContainer.getChildren().add(reviewBox);
            }
        }
    }

    // Загрузка отзывов при инициализации
    @FXML
    private void initialize() {
        loadReviews();

        // Устанавливаем начальный выбор в ComboBox
        sortComboBox.setValue("От 1 до 5");
    }

    // Метод для добавления отзыва
    @FXML
    private void addReview() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addReviewsCL.fxml"));
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

    // Метод для возвращения на основной экран
    @FXML
    private void handleBack() {
        try {
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

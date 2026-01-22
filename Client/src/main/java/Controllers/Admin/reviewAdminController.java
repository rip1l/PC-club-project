package Controllers.Admin;

import POJO.Review;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class reviewAdminController {
    @FXML
    private VBox mainContainer;
    @FXML
    private VBox reviewsListContainer;
    @FXML
    private ComboBox<String> sortComboBox;
    private List<Review> reviews;

    private void loadReviews() {
        ConnectDB.user.sendMessage("viewReviews");
        reviews = (List<Review>) ConnectDB.user.readObject();

        if (reviews != null) {
            reviewsListContainer.getChildren().clear();
            for (Review review : reviews) {
                HBox reviewBox = createReviewBox(review);
                reviewsListContainer.getChildren().add(reviewBox);
            }
        }
    }

    private HBox createReviewBox(Review review) {
        HBox reviewBox = new HBox(15);
        reviewBox.setStyle("-fx-background-color: #252525; " +
                "-fx-padding: 15; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #9c4dff; " +
                "-fx-border-width: 1; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(156,77,255,0.3), 5, 0, 0, 0);");

        // Текст отзыва
        Label reviewText = new Label(review.getReviewText());
        reviewText.setFont(new Font(14));
        reviewText.setStyle("-fx-text-fill: #e0e0e0; -fx-wrap-text: true; -fx-max-width: 500;");

        // Оценка в виде звезд
        HBox ratingBox = new HBox(5);
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView("/images/star" + (i <= review.getRating() ? "filled" : "empty") + ".png");
            star.setFitWidth(20);
            star.setFitHeight(20);
            ratingBox.getChildren().add(star);
        }

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> deleteReview(review));
        deleteButton.setStyle("-fx-background-color: #9c4dff; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 5 15;");

        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #7b3dcc; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 5 15;"));

        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #9c4dff; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 5 15;"));

        reviewBox.getChildren().addAll(reviewText, ratingBox, deleteButton);
        return reviewBox;
    }

    private void deleteReview(Review review) {
        ConnectDB.user.sendMessage("deleteReview");
        ConnectDB.user.sendObject(review.getReviewId());
        String response = (String) ConnectDB.user.readObject();

        if ("Review deleted successfully".equals(response)) {
            showAlert("Успех", "Отзыв успешно удален.");
            loadReviews();
        } else {
            showAlert("Ошибка", "Не удалось удалить отзыв.");
        }
    }

    @FXML
    private void handleSortButtonClick() {
        String selectedSort = sortComboBox.getValue();

        if (reviews != null) {
            if ("От 1 до 5".equals(selectedSort)) {
                reviews = reviews.stream()
                        .sorted(Comparator.comparingInt(Review::getRating))
                        .collect(Collectors.toList());
            } else if ("От 5 до 1".equals(selectedSort)) {
                reviews = reviews.stream()
                        .sorted((r1, r2) -> Integer.compare(r2.getRating(), r1.getRating()))
                        .collect(Collectors.toList());
            }

            reviewsListContainer.getChildren().clear();
            for (Review review : reviews) {
                HBox reviewBox = createReviewBox(review);
                reviewsListContainer.getChildren().add(reviewBox);
            }
        }
    }

    @FXML
    private void initialize() {
        // Настройка стиля ComboBox
        sortComboBox.setStyle("-fx-background-color: #2b2b2b; " +
                "-fx-text-fill: white; " +
                "-fx-border-color: #9c4dff; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;");

        loadReviews();
        sortComboBox.setValue("От 1 до 5");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuAdmin.fxml"));
            Parent registerPage = loader.load();
            Scene registerScene = new Scene(registerPage);

            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setWidth(900);  // Соответствует размеру из menuAdmin.fxml
            stage.setHeight(700); // Соответствует размеру из menuAdmin.fxml
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Применение темной темы к Alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #1a1a1a; " +
                "-fx-text-fill: #e0e0e0; " +
                "-fx-border-color: #9c4dff; " +
                "-fx-border-width: 2;");

        alert.showAndWait();
    }
}
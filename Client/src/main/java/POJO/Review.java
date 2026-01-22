package POJO;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Review implements Serializable {
    private static final long serialVersionUID = 1L;
    private int reviewId;       // Идентификатор отзыва
    private int usersId;        // Внешний ключ из таблицы users (замена clientId на usersId)
    private String reviewText;  // Текст отзыва
    private int rating;         // Оценка (1–5)
    private LocalDateTime createdAt; // Дата создания отзыва

    // Конструктор без параметров (нужен для сериализации/десериализации)
    public Review() {}

    // Полный конструктор
    public Review(int reviewId, int usersId, String reviewText, int rating, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.usersId = usersId;  // Используем usersId вместо clientId
        this.reviewText = reviewText;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUsersId() {  // Изменено с clientId на usersId
        return usersId;
    }

    public void setUsersId(int usersId) {  // Изменено с clientId на usersId
        this.usersId = usersId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Метод для вывода информации об отзыве
    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", usersId=" + usersId +  // Используем usersId вместо clientId
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                '}';
    }
}

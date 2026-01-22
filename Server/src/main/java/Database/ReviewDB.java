package Database;

import POJO.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDB implements IntReviewDB {
    private static ReviewDB instance;
    private DatabaseConnection dbConnection;

    private ReviewDB() throws SQLException, ClassNotFoundException {
        dbConnection = DatabaseConnection.getInstance();
    }

    public static ReviewDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new ReviewDB();
        }
        return instance;
    }

    @Override
    public boolean addReview(Review review) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO reviews (user_id, review_text, rating) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            stmt.setInt(1, review.getUsersId());
            stmt.setString(2, review.getReviewText());
            stmt.setInt(3, review.getRating());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    @Override
    public List<Review> getAllReviews() throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews";  // Без сортировки
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setUsersId(rs.getInt("user_id"));
                review.setReviewText(rs.getString("review_text"));
                review.setRating(rs.getInt("rating"));
                reviews.add(review);
            }
        }
        return reviews;
    }
    @Override
    public boolean deleteReview(int reviewId) throws SQLException {
        String query = "DELETE FROM reviews WHERE review_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
            stmt.setInt(1, reviewId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}


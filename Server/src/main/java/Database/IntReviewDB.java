package Database;

import POJO.Review;

import java.sql.SQLException;
import java.util.List;

public interface IntReviewDB {
    boolean addReview(Review review) throws SQLException, ClassNotFoundException;
    boolean deleteReview(int reviewId) throws SQLException;
    List<Review> getAllReviews() throws SQLException;
}

package Database;

import POJO.*;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class BookingDB implements IntBookingDB {
    private static BookingDB instance;
    private DatabaseConnection dbConnection;

    private BookingDB() throws SQLException, ClassNotFoundException {
        dbConnection = DatabaseConnection.getInstance();
    }

    public static BookingDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new BookingDB();
        }
        return instance;
    }

    @Override
    public boolean createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (client_id, computer_id, tariff_id, start_time, end_time, " +
                "total_price, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getClientId());
            pstmt.setInt(2, booking.getComputerId());
            pstmt.setInt(3, booking.getTariffId());
            pstmt.setTimestamp(4, Timestamp.valueOf(booking.getStartTime()));
            pstmt.setTimestamp(5, Timestamp.valueOf(booking.getEndTime()));
            pstmt.setDouble(6, booking.getTotalPrice());
            pstmt.setString(7, booking.getStatus());
            pstmt.setTimestamp(8, Timestamp.valueOf(booking.getCreatedAt()));

            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cancelBooking(int bookingId) throws SQLException {
        String sql = "UPDATE bookings SET status = 'cancelled' WHERE booking_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Booking> getClientBookings(int clientId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE client_id = ? ORDER BY start_time DESC";
        List<Booking> bookings = new ArrayList<>();

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapBookingFromResultSet(rs));
                }
            }
        }
        return bookings;
    }

    @Override
    public List<Booking> getComputerBookings(int computerId, LocalDate date) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE computer_id = ? AND DATE(start_time) = ? ORDER BY start_time";
        List<Booking> bookings = new ArrayList<>();

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, computerId);
            pstmt.setDate(2, Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapBookingFromResultSet(rs));
                }
            }
        }
        return bookings;
    }
    @Override
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings ORDER BY start_time DESC";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Booking booking = new Booking(
                            rs.getInt("booking_id"),
                            rs.getInt("client_id"),
                            rs.getInt("computer_id"),
                            rs.getInt("tariff_id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime(),
                            rs.getDouble("total_price"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }
    @Override
    public boolean updateBookingStatus(int bookingId, String status) throws SQLException {
        String query = "UPDATE bookings SET status = ? WHERE booking_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);

            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean checkComputerAvailability(int computerId, LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE computer_id = ? AND " +
                "((start_time < ? AND end_time > ?) OR " +
                "(start_time >= ? AND start_time < ?)) AND status != 'cancelled'";

        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, computerId);
            pstmt.setTimestamp(2, Timestamp.valueOf(end));
            pstmt.setTimestamp(3, Timestamp.valueOf(start));
            pstmt.setTimestamp(4, Timestamp.valueOf(start));
            pstmt.setTimestamp(5, Timestamp.valueOf(end));

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }

    private Booking mapBookingFromResultSet(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("booking_id"),
                rs.getInt("client_id"),
                rs.getInt("computer_id"),
                rs.getInt("tariff_id"),
                rs.getTimestamp("start_time").toLocalDateTime(),
                rs.getTimestamp("end_time").toLocalDateTime(),
                rs.getDouble("total_price"),
                rs.getString("status"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
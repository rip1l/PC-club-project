package Database;

import POJO.Booking;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IntBookingDB {
    boolean createBooking(Booking booking) throws SQLException;
    boolean cancelBooking(int bookingId) throws SQLException;
    List<Booking> getClientBookings(int clientId) throws SQLException;
    List<Booking> getAllBookings() throws SQLException;
    List<Booking> getComputerBookings(int computerId, LocalDate date) throws SQLException; // Используем LocalDate
    boolean checkComputerAvailability(int computerId, LocalDateTime start, LocalDateTime end) throws SQLException;
    boolean updateBookingStatus(int bookingId, String status) throws SQLException;
}
package Database;

import java.sql.SQLException;

public abstract class Factory {
    public abstract IntAuthDB getRole() throws SQLException, ClassNotFoundException;
    public abstract IntUsersDB getUsers() throws SQLException, ClassNotFoundException;
    public abstract IntClientsDB getClientsDB() throws SQLException, ClassNotFoundException;
    public abstract IntReviewDB getReviewDB() throws SQLException, ClassNotFoundException;
    public abstract IntComputerDB getComputerDB() throws SQLException, ClassNotFoundException;
    public abstract IntGroupDB getGroupDB() throws SQLException, ClassNotFoundException;
    public abstract IntTariffDB getTariffDB() throws SQLException, ClassNotFoundException;
    public abstract IntBookingDB getBookingDB() throws SQLException, ClassNotFoundException;
}

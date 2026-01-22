package Database;

import java.sql.SQLException;

public class FactoryDB extends Factory {

    public UsersDB getUsers() throws SQLException, ClassNotFoundException {
        return UsersDB.getInstance();
    }
    public AuthDB getRole() throws SQLException, ClassNotFoundException {
        return AuthDB.getInstance();
    }
    public  IntClientsDB getClientsDB() throws SQLException, ClassNotFoundException{
        return ClientsDB.getInstance();
    }
    public IntReviewDB getReviewDB() throws SQLException, ClassNotFoundException{
        return ReviewDB.getInstance();
    }
    public IntBookingDB getBookingDB() throws SQLException, ClassNotFoundException{
        return BookingDB.getInstance();
    }
    public IntComputerDB getComputerDB() throws SQLException, ClassNotFoundException{
        return ComputerDB.getInstance();
    }
    public IntTariffDB getTariffDB() throws SQLException, ClassNotFoundException{
        return TariffDB.getInstance();
    }
    public  IntGroupDB getGroupDB() throws SQLException, ClassNotFoundException{
        return GroupDB.getInstance();
    }
}

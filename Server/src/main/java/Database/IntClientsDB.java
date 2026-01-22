package Database;

import java.sql.SQLException;
import POJO.Clients;

public interface IntClientsDB {
    boolean isClientInfoExists(int userId) throws SQLException;
    boolean insert(Clients client) throws SQLException;
    Clients getClientByUserId(int userId) throws SQLException, ClassNotFoundException;
    boolean updateLogin(int userId, String newLogin) throws SQLException;
    boolean updateClientInfo(int userId, String lastName, String firstName, String middleName, String phoneNumber) throws SQLException;
    int getClientIdByUserId(int usersId) throws SQLException;
}

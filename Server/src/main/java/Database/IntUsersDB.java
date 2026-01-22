package Database;

import POJO.Users;

import java.sql.SQLException;
import java.util.List;

public interface IntUsersDB {
    boolean insert(Users obj) throws SQLException, ClassNotFoundException;
    // Удаление пользователя по ID
    boolean deleteUserByLogin(String login) throws SQLException;
    // Получение списка всех пользователей
    List<Users> getAllUsers() throws SQLException;
    // Обновление баланса пользователя
    boolean updateUserBalance(int userId, double amount) throws SQLException;
    boolean updateUserRoleByLogin(String login, String newRole) throws SQLException;
    double getBalanceByUserId(int UserId) throws SQLException;
}

package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import POJO.Users;
import Database.DatabaseConnection;

public class UsersDB implements IntUsersDB {
    private static UsersDB instance;
    private DatabaseConnection dbConnection;

    public UsersDB() throws SQLException, ClassNotFoundException {
        dbConnection = DatabaseConnection.getInstance();
    }

    public static synchronized UsersDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new UsersDB();
        }
        return instance;
    }

    @Override
    public boolean insert(Users obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";  // SQL-запрос для вставки данных
        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, obj.getLogin());   // Логин
            pstmt.setString(2, obj.getPassword()); // Пароль
            pstmt.setString(3, obj.getRole());     // Роль

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("Ошибка: Пользователь с таким логином уже существует.");
            } else {
                e.printStackTrace();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserExists(String login) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE login = ?";
        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;  // Если результат больше 0, то пользователь существует
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
@Override
public boolean deleteUserByLogin(String login) throws SQLException {
    String query = "DELETE FROM users WHERE login = ?";
    try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
        stmt.setString(1, login);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }
}
@Override
    public boolean updateUserRoleByLogin(String login, String newRole) throws SQLException {
        String query = "UPDATE users SET role = ? WHERE login = ?";
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
            stmt.setString(1, newRole);
            stmt.setString(2, login);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


    @Override
    public List<Users> getAllUsers() throws SQLException {
        String sql = "SELECT user_id, login, password, role, balance FROM users";
        List<Users> usersList = new ArrayList<>();
        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                double balance = resultSet.getDouble("balance");
                usersList.add(new Users(id, login, password, role,balance));
            }
        }
        return usersList;
    }

    @Override
    public boolean updateUserBalance(int userId, double amount) throws SQLException {
        String sql = "UPDATE users SET balance = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.dbConnection.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public double getBalanceByUserId(int userId) throws SQLException {
        String query = "SELECT balance FROM users WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnection.dbConnection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
            return 0.0;
        }
    }
}

package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import POJO.Clients;
import POJO.Users;

public class ClientsDB implements IntClientsDB {
    private static ClientsDB instance;
    private DatabaseConnection dbConnection;

    private ClientsDB() throws SQLException, ClassNotFoundException {
        dbConnection = DatabaseConnection.getInstance();
    }

    public static ClientsDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new ClientsDB();
        }
        return instance;
    }
    @Override
    public boolean isClientInfoExists(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM clients WHERE user_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;  // Если больше 0, значит информация уже добавлена
        }
    }

    @Override
    public boolean insert(Clients client) throws SQLException {
        String query = "INSERT INTO clients (user_id, last_name, first_name, middle_name, phone_number) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
            stmt.setInt(1, client.getUserId());
            stmt.setString(2, client.getLastName());
            stmt.setString(3, client.getFirstName());
            stmt.setString(4, client.getMiddleName());
            stmt.setString(5, client.getPhoneNumber());
            return stmt.executeUpdate() > 0;  // Если добавлено, возвращаем true
        }
    }
    @Override
    public Clients getClientByUserId(int userId) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM clients WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnection.dbConnection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Clients client = new Clients();
                client.setClientId(resultSet.getInt("client_id"));
                client.setUserId(resultSet.getInt("user_id"));
                client.setLastName(resultSet.getString("last_name"));
                client.setFirstName(resultSet.getString("first_name"));
                client.setMiddleName(resultSet.getString("middle_name"));
                client.setPhoneNumber(resultSet.getString("phone_number"));
                return client;
            } else {
                return null;
            }
        }
    }
    @Override
    public boolean updateLogin(int userId, String newLogin) throws SQLException {
        String query = "UPDATE users SET login = ? WHERE user_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
            stmt.setString(1, newLogin);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;  // Если обновлено, возвращаем true
        }
    }
@Override
    // Метод для обновления других данных клиента
    public boolean updateClientInfo(int userId, String lastName, String firstName, String middleName, String phoneNumber) throws SQLException {
        String query = "UPDATE clients SET last_name = ?, first_name = ?, middle_name = ?, phone_number = ? WHERE user_id = ?";
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            stmt.setString(4, phoneNumber);
            stmt.setInt(5, userId);
            return stmt.executeUpdate() > 0;  // Если обновлено, возвращаем true
        }
    }
    @Override
    public int getClientIdByUserId(int usersId) throws SQLException {
        String query = "SELECT client_id FROM clients WHERE user_id = ?";

        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {
            stmt.setInt(1, usersId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("client_id");
                } else {
                    return -1;  // Если не найдено
                }
            }
        }
    }
}

package Database;

import POJO.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AuthDB implements IntAuthDB {
    private static AuthDB instance;
    private DatabaseConnection dbConnection;

    private AuthDB() throws SQLException, ClassNotFoundException {
        dbConnection = DatabaseConnection.getInstance();
    }

    public static synchronized AuthDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new AuthDB();
        }
        return instance;
    }

    @Override
    public Users getRole(Auth obj) {
        String query = "SELECT user_id, login, password, role FROM users WHERE login = ?";
        Users users = null;
        try (PreparedStatement stmt = DatabaseConnection.dbConnection.prepareStatement(query)) {  // Используем dbConnection
            stmt.setString(1, obj.getLogin());  // Заменяем auth на obj
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // Сравниваем хеш пароля из базы данных с переданным паролем
                if (BCrypt.checkpw(obj.getPassword(), storedPassword)) {
                    users = new Users();
                    users.setId(rs.getInt("user_id"));
                    users.setLogin(rs.getString("login"));
                    users.setRole(rs.getString("role"));
                } else {
                    // Неверный пароль
                    users = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}

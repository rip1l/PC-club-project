import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import Database.DatabaseConnection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseConnectionTest {

    private DatabaseConnection dbConnection;

    @BeforeEach
    public void setUp() throws Exception {
        try {
            // Инициализация соединения с базой данных через наш класс DatabaseConnection
            dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            fail("Unable to connect to database: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (dbConnection != null) {
            // Очистка ресурсов, если соединение открыто
            dbConnection.dbConnection.close();
        }
    }

    @Test
    public void testConnection() throws Exception {
        // Проверка, что соединение установлено и действительно
        assertTrue(dbConnection.dbConnection.isValid(5));
    }
}

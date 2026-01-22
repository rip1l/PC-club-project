import Database.UsersDB;
import POJO.Users;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UsersDBTest {

    private UsersDB usersDB;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        usersDB = mock(UsersDB.class);
    }

    @Test
    public void testInsertUser() throws SQLException, ClassNotFoundException {
        Users newUser = new Users(1, "testLogin", "123qweasd", "admin");

        when(usersDB.insert(newUser)).thenReturn(true);
        boolean result = usersDB.insert(newUser);

        assertTrue(result, "Пользователь должен быть успешно добавлен");
    }

    @Test
    public void testIsUserExists() throws SQLException {
        String login = "testLogin";

        when(usersDB.isUserExists(login)).thenReturn(true);

        assertTrue(usersDB.isUserExists(login), "Пользователь должен существовать");

        when(usersDB.isUserExists("nonExistentLogin")).thenReturn(false);

        assertFalse(usersDB.isUserExists("nonExistentLogin"), "Пользователь не должен существовать");
    }

    @Test
    public void testDeleteUserByLogin() throws SQLException {
        String login = "testLogin";

        when(usersDB.deleteUserByLogin(login)).thenReturn(true);

        assertTrue(usersDB.deleteUserByLogin(login), "Пользователь должен быть удален");
    }

    @Test
    public void testUpdateUserRoleByLogin() throws SQLException {
        String login = "testLogin";
        String newRole = "manager";

        when(usersDB.updateUserRoleByLogin(login, newRole)).thenReturn(true);

        assertTrue(usersDB.updateUserRoleByLogin(login, newRole), "Роль пользователя должна быть обновлена");
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        List<Users> usersList = List.of(
                new Users(1, "testLogin1", "password123", "admin"),
                new Users(2, "testLogin2", "password456", "user")
        );

        when(usersDB.getAllUsers()).thenReturn(usersList);

        List<Users> result = usersDB.getAllUsers();

        assertNotNull(result, "Список пользователей не должен быть null");
        assertEquals(2, result.size(), "Количество пользователей должно быть 2");
    }

    @AfterEach
    public void tearDown() {
        usersDB = null;
    }
}

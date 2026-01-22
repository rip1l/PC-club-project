package Controllers;

import java.io.IOException;

import POJO.Users;
import ServerWORK.ConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button ExitButton;

    @FXML
    void initialize() {
        registerButton.setOnAction(event -> registerUser());
    }

    @FXML
    void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            showAlertWithPasswordMismatch();
            return;
        }

        if (password.length() < 8) {
            showAlertWithShortPassword();
            return;
        }

        if (username.isEmpty()) {
            showAlertWithEmptyUsername();
            return;
        }

        Users users = new Users();
        users.setLogin(username);
        users.setPassword(hashPassword(password)); // Хешируем пароль
        users.setRole("client");

        ConnectDB.user.sendMessage("registrationClient");
        ConnectDB.user.sendObject(users);
        System.out.println("Запись отправлена");

        String mes = "";
        try {
            mes = ConnectDB.user.readMessage();
        } catch (IOException ex) {
            System.out.println("Error in reading");
        }

        if (mes.equals("This user already exists")) {
            showAlertWithExistLogin();
        } else if (mes.equals("OK")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/authorisation.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("BarberShop_Client");
                stage.setScene(new Scene(root));
                stage.setWidth(800);
                stage.setHeight(600);
                stage.show();

                // Закрытие окна регистрации
                ((Stage) registerButton.getScene().getWindow()).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlertWithError();
        }
    }

    // Метод для показа ошибки, если логин уже существует
    static public void showAlertWithExistLogin() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Регистрация");
        alert.setContentText("Такой пользователь уже существует");
        alert.showAndWait();
    }

    // Метод для показа ошибки при несоответствии паролей
    static public void showAlertWithPasswordMismatch() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Пароли не совпадают");
        alert.setContentText("Пароли не совпадают. Пожалуйста, попробуйте снова.");
        alert.showAndWait();
    }

    // Метод для показа ошибки, если пароль слишком короткий
    static public void showAlertWithShortPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Пароль");
        alert.setContentText("Пароль должен содержать не менее 8 символов.");
        alert.showAndWait();
    }

    // Метод для показа ошибки, если имя пользователя пустое
    static public void showAlertWithEmptyUsername() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Имя пользователя");
        alert.setContentText("Логин не может быть пустым.");
        alert.showAndWait();
    }

    // Метод для показа ошибки, если произошла ошибка при регистрации
    static public void showAlertWithError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Регистрация");
        alert.setContentText("Произошла ошибка при регистрации. Попробуйте позже.");
        alert.showAndWait();
    }

    // Метод для хеширования пароля с использованием BCrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());  // Генерация соли и хеширование пароля
    }

    @FXML
    void handleExit(ActionEvent event){
        SceneLoader.loadScene((Stage) ExitButton.getScene().getWindow(), "/mainpage.fxml", 800, 600);
    }
}

package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ServerWORK.*;
import POJO.*;

import java.io.*;
import java.net.Socket;
import java.util.Objects;


public class AuthorisationController {
    @FXML
    private VBox mainContainer;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button ExitButton;

    @FXML
    void handleLogin(ActionEvent event) throws IOException {
            ConnectDB.user.sendMessage("authorization");
            Auth auth = new Auth();
            auth.setLogin(usernameField.getText());
            auth.setPassword(passwordField.getText());
        ConnectDB.user.sendObject(auth);

            String mes = "";
            try {
                mes = ConnectDB.user.readMessage();
            } catch (IOException ex) {
                System.out.println("Error in reading");
            }
            if (mes.equals("There is no data!"))
                showAlertWithNoLogin();
            else {
                Users users = (Users) ConnectDB.user.readObject();
                ConnectDB.id = users.getId();
                ConnectDB.login = users.getLogin();
                ConnectDB.role = users.getRole();
                ConnectDB.balance = users.getBalance();
                loginButton.getScene().getWindow().hide();
                System.out.println(ConnectDB.role);

                if (Objects.equals(ConnectDB.role, "client")) {
                    System.out.println("Окно клиента");
                    SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/windowClient.fxml", 800, 800);
                }
                else
                SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/menuAdmin.fxml", 800, 800);
            }
    }

    @FXML
    void handleExit(ActionEvent event){
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/mainpage.fxml", 800, 600);
    }

    static public void showAlertWithNoLogin(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка: Введите правильно логин или пароль");
        alert.setContentText("Такой пользователь не найден в системе");
        alert.showAndWait();
    }
}

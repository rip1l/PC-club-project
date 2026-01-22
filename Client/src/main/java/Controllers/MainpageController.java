package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainpageController {

    @FXML
    private VBox mainContainer;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void handleLogin() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/authorisation.fxml", 800, 600);
    }

    @FXML
    public void handleRegister() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/register.fxml", 800, 600);
    }
}

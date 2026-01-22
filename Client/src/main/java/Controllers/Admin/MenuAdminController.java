package Controllers.Admin;

import Controllers.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


import java.io.*;

public class MenuAdminController {
    @FXML
    private VBox mainContainer;
    @FXML
    private Button service;
    @FXML
    private Button reviews;
    @FXML
    private Button user;
    @FXML
    private Button tariff;
    @FXML
    private Button ourInfo;
    @FXML
    private Button backButton;
    @FXML
    private Button order;
    @FXML
    public void handleComputer() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/computerManager.fxml", 900, 600);
    }
    @FXML
    public void handleGroup() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/groupManagment.fxml", 900, 600);
    }
    @FXML
    public void handleBooking() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/bookingManagement.fxml", 800, 600);
    }
    @FXML
    public void handleOurInfo() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/adminOurInfo.fxml", 1000, 800);
    }
    @FXML
    public void handleTariff() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/tariffsManagement.fxml", 800, 600);
    }
    @FXML
    public void handleUser() {
        SceneLoader.loadScene((Stage) mainContainer.getScene().getWindow(), "/workUsersAdmin.fxml", 800, 600);

    }
    @FXML
    public void handleReviews() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reviewAdmin.fxml"));
            Parent registerPage = loader.load();
            Scene registerScene = new Scene(registerPage);

            // Получаем текущее окно и устанавливаем новую сцену
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack()
    {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainpage.fxml"));
            Parent registerPage = loader.load();
            Scene registerScene = new Scene(registerPage);

            // Получаем текущее окно и устанавливаем новую сцену
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


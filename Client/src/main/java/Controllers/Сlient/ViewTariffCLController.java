package Controllers.Сlient;

import POJO.Tariff;
import ServerWORK.ConnectDB;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

import java.io.IOException;

public class ViewTariffCLController {

    @FXML
    private Button backButton;

    @FXML
    private VBox mainContainer;

    @FXML
    private VBox rateTemplate;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private VBox servicesContainer;

    private List<Tariff> tariffs;

    @FXML
    void initialize() {
        loadTariffs();
    }

    private void loadTariffs() {
        try {
            // Отправляем запрос на сервер
            ConnectDB.user.sendMessage("get_all_tariffs");

            // Получаем ответ
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                tariffs = (List<Tariff>) response;

                if (tariffs != null && !tariffs.isEmpty()) {
                    Platform.runLater(() -> {
                        servicesContainer.getChildren().clear();
                        for (Tariff tariff : tariffs) {
                            HBox tariffBox = createTariffBox(tariff);
                            servicesContainer.getChildren().add(tariffBox);
                        }
                    });
                    return;
                }
            }

            Platform.runLater(() -> {
                showNoTariffsMessage("Тарифы не найдены");
            });

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                showNoTariffsMessage("Ошибка при загрузке тарифов");
            });
        }
    }

    private void showNoTariffsMessage(String message) {
        servicesContainer.getChildren().clear();
        Label label = new Label(message != null ? message : "Тарифы не найдены");
        label.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 16px;");
        servicesContainer.getChildren().add(label);
    }

    // Создание карточки тарифа
    private HBox createTariffBox(Tariff tariff) {
        HBox tariffBox = new HBox(20);
        tariffBox.setAlignment(Pos.CENTER_LEFT);
        tariffBox.setStyle(rateTemplate.getStyle());
        tariffBox.setMinWidth(900);

        // Левая часть с названием
        VBox leftBox = new VBox(5);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setMinWidth(300);

        Label nameLabel = new Label(tariff.getName());
        nameLabel.setStyle("-fx-text-fill: #9c4dff; -fx-font-size: 20px; -fx-font-weight: bold;");

        leftBox.getChildren().add(nameLabel);

        // Центральная часть с ценой
        VBox centerBox = new VBox(5);
        centerBox.setAlignment(Pos.CENTER_RIGHT);
        centerBox.setMinWidth(200);

        Label priceTitle = new Label("Цена за час:");
        priceTitle.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 14px;");

        Label priceLabel = new Label(tariff.getPricePerHour() + " BYN");
        priceLabel.setStyle("-fx-text-fill: #9c4dff; -fx-font-size: 18px; -fx-font-weight: bold;");

        centerBox.getChildren().addAll(priceTitle, priceLabel);

        // Собираем все вместе
        tariffBox.getChildren().addAll(leftBox, centerBox);

        return tariffBox;
    }

    @FXML
    void applySearch(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            loadTariffs(); // Перезагружаем все тарифы если поле поиска пустое
            return;
        }

        // Фильтруем тарифы по введенному тексту
        List<Tariff> filteredTariffs = tariffs.stream()
                .filter(t -> t.getName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        // Очищаем контейнер и добавляем отфильтрованные тарифы
        servicesContainer.getChildren().clear();
        for (Tariff tariff : filteredTariffs) {
            HBox tariffBox = createTariffBox(tariff);
            servicesContainer.getChildren().add(tariffBox);
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/windowClient.fxml"));
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
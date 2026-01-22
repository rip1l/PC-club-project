package Controllers.Admin;

import POJO.Services;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class ViewServiceController {

    @FXML
    private FlowPane servicesFlowPane;

    @FXML
    private TextField searchField;

    @FXML
    private TextField minPriceField;

    @FXML
    private TextField maxPriceField;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private Button backButton;

    private List<Services> allServices; // Хранит все услуги для обработки

    @FXML
    void initialize() {
        loadServices();
    }

    private void loadServices() {
        try {
            ConnectDB.user.sendMessage("viewServices");
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                allServices = (List<Services>) response;
                displayServices(allServices);
            } else {
                System.out.println("Received response is not a List.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить данные. Попробуйте позже.");
        }
    }

    private void displayServices(List<Services> services) {
        servicesFlowPane.getChildren().clear();
        for (Services service : services) {
            VBox serviceBox = new VBox(10);
            serviceBox.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4; -fx-border-color: black; -fx-border-radius: 10;");

            Label serviceName = new Label(service.getName());
            serviceName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

            Text description = new Text(service.getDescription());
            Text price = new Text("Цена: " + service.getPrice() + " руб.");
            Text duration = new Text("Продолжительность: " + service.getDuration());

            serviceBox.getChildren().addAll(serviceName, description, price, duration);
            servicesFlowPane.getChildren().add(serviceBox);
        }
    }

    @FXML
    private void applySearch() {
        String query = searchField.getText().toLowerCase();
        List<Services> filteredServices = allServices.stream()
                .filter(service -> service.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());
        displayServices(filteredServices);
    }

    @FXML
    private void applyFilter() {
        try {
            double minPrice = minPriceField.getText().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minPriceField.getText());
            double maxPrice = maxPriceField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceField.getText());

            List<Services> filteredServices = allServices.stream()
                    .filter(service -> service.getPrice() >= minPrice && service.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
            displayServices(filteredServices);
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные значения для фильтрации.");
        }
    }

    @FXML
    private void applySort() {
        String sortOrder = sortComboBox.getValue();
        if (sortOrder != null) {
            List<Services> sortedServices = new ArrayList<>(allServices);
            if (sortOrder.equals("От А до Я")) {
                sortedServices.sort(Comparator.comparing(Services::getName));
            } else if (sortOrder.equals("От Я до А")) {
                sortedServices.sort(Comparator.comparing(Services::getName).reversed());
            }
            displayServices(sortedServices);
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/serviceAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Админ панель");
            stage.setScene(new Scene(root));
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();

            ((Stage) backButton.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private static final String HOVER_STYLE = "-fx-background-radius: 15; " +
            "-fx-border-radius: 15; " +
            "-fx-font-family: 'Georgia'; " +
            "-fx-font-size: 16; " +
            "-fx-pref-width: 130; " +
            "-fx-pref-height: 40; " +
            "-fx-background-color: #333333; " + // Тёмный фон
            "-fx-text-fill: #FFF8DC; " +       // Светлый текст
            "-fx-border-color: black; " +
            "-fx-border-width: 2;";

    // Исходный стиль
    private static final String DEFAULT_STYLE = "-fx-background-radius: 15; " +
            "-fx-border-radius: 15; " +
            "-fx-font-family: 'Georgia'; " +
            "-fx-font-size: 16; " +
            "-fx-pref-width: 130; " +
            "-fx-pref-height: 40; " +
            "-fx-background-color: white; " + // Прозрачный фон
            "-fx-text-fill: black; " +              // Чёрный текст
            "-fx-border-color: black; " +
            "-fx-border-width: 2;";

    @FXML
    private void handleMouseEnterExit(javafx.scene.input.MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(HOVER_STYLE); // Устанавливаем стиль при наведении
    }

    @FXML
    private void handleMouseExitExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle(DEFAULT_STYLE); // Возвращаем исходный стиль
    }
}

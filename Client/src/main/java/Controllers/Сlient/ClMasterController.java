package Controllers.Сlient;

import POJO.Employee;
import POJO.Services;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClMasterController {
    @FXML
    private VBox mainContainer;
    @FXML
    private FlowPane servicesFlowPane;
    @FXML
    private TextField searchField;
    @FXML
    private Button sortButton;
    @FXML
    private Button filterButton;
    @FXML
    private TextField minPriceField;
    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private TextField maxPriceField;
    @FXML
    private Button backButton;
    private List<Employee> allEmployees;
    @FXML
    void initialize() {
        loadEmployees();
    }
    private void loadEmployees() {
        try {
            ConnectDB.user.sendMessage("viewEmployees");
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                allEmployees = (List<Employee>) response;
                displayEmployees(allEmployees);
            } else {
                System.out.println("Received response is not a List.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось загрузить данные. Попробуйте позже.");
        }
    }
    private void displayEmployees(List<Employee> employees) {
        servicesFlowPane.getChildren().clear();
        for (Employee employee : employees) {
            VBox serviceBox = new VBox(10);
            serviceBox.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4; -fx-border-color: black; -fx-border-radius: 10;");
            javafx.scene.control.Label empName = new javafx.scene.control.Label(employee.getLastName());
            empName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
            javafx.scene.control.Label empFirstName = new javafx.scene.control.Label(employee.getFirstName());
            empFirstName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
            javafx.scene.control.Label empSpez = new javafx.scene.control.Label(employee.getSpecialization());
            empFirstName.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

            Text price = new Text("Стаж: " + employee.getExperienceYears() + " год");
            Text duration = new Text("Время работы: " + employee.getWorkStartTime() +" - "+ employee.getWorkEndTime());

            serviceBox.getChildren().addAll(empName,empFirstName,empSpez , price, duration);
            servicesFlowPane.getChildren().add(serviceBox);
        }
    }
    @FXML
    private void applySearch() {
        String query = searchField.getText().toLowerCase();
        List<Employee> filteredServices = allEmployees.stream()
                .filter(employee -> employee.getSpecialization().toLowerCase().contains(query))
                .collect(Collectors.toList());
        displayEmployees(filteredServices);
    }
    @FXML
    private void applyFilter() {
        try {
            double minPrice = minPriceField.getText().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(minPriceField.getText());
            double maxPrice = maxPriceField.getText().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxPriceField.getText());

            List<Employee> filteredServices = allEmployees.stream()
                    .filter(employee -> employee.getExperienceYears() >= minPrice && employee.getExperienceYears() <= maxPrice)
                    .collect(Collectors.toList());
            displayEmployees(filteredServices);
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные значения для фильтрации.");
        }
    }
    @FXML
    private void applySort() {
        String sortOrder = sortComboBox.getValue();
        if (sortOrder != null) {
            List<Employee> sortedServices = new ArrayList<>(allEmployees);
            if (sortOrder.equals("От А до Я")) {
                sortedServices.sort(Comparator.comparing(Employee::getLastName));
            } else if (sortOrder.equals("От Я до А")) {
                sortedServices.sort(Comparator.comparing(Employee::getLastName).reversed());
            }
            displayEmployees(sortedServices);
        }
    }
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/windowClient.fxml"));
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


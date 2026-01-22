package Controllers.Admin;

import POJO.Tariff;
import ServerWORK.ConnectDB;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TariffEditDialogController {

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private Button applyButton;
    @FXML private Button cancelButton;

    private Tariff tariff;
    private boolean saved = false;

    @FXML
    public void initialize() {
        // Обработчики для кнопок
        applyButton.setOnAction(event -> handleApply());
        cancelButton.setOnAction(event -> closeWindow());
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
        if (tariff != null) {
            nameField.setText(tariff.getName());
            priceField.setText(String.valueOf(tariff.getPricePerHour()));
        }
    }

    public boolean isSaved() {
        return saved;
    }

    private void handleApply() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());

            if (name.isEmpty()) {
                showAlert("Ошибка", "Название тарифа не может быть пустым");
                return;
            }

            Tariff updatedTariff = new Tariff();
            updatedTariff.setName(name);
            updatedTariff.setPricePerHour(price);

            if (tariff != null) {
                updatedTariff.setTariffId(tariff.getTariffId());
            }

            ConnectDB.user.sendMessage("update_tariff");
            ConnectDB.user.sendObject(updatedTariff);

            String response = (String) ConnectDB.user.readObject();

            if ("OK".equals(response)) {
                saved = true;
                closeWindow();
            } else {
                showAlert("Ошибка", response);
            }
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректную цену");
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось обновить тариф: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        ((Stage) nameField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
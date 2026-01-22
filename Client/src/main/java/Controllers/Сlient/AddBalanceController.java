package Controllers.Сlient;

import POJO.ClientInfo;
import POJO.Clients;
import ServerWORK.ConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AddBalanceController {

    @FXML
    private Button addBalanceButton;

    @FXML
    private TextField amountField;

    @FXML
    private Button backButton;

    @FXML
    private VBox mainContainer;

    @FXML
    private Label newBalanceLabel;

    @FXML
    private ComboBox<String> paymentMethodComboBox;

    @FXML
    public void initialize() {
        paymentMethodComboBox.getItems().addAll("Банковская карта", "Наличные", "Криптовалюта");
        int userId = ConnectDB.id;  // Получаем ID текущего пользователя из ConnectDB
        if (userId == 0) {
            showAlert("Ошибка", "Не удалось получить данные пользователя.");
            return;
        }
        ConnectDB.user.sendMessage("viewClientInfo");  // Отправляем команду на сервер
        ConnectDB.user.sendObject(userId);  // Отправляем ID пользователя

        // Получаем объект клиента с сервера
        ClientInfo client = (ClientInfo) ConnectDB.user.readObject();

        if (client != null) {
            ConnectDB.balance = client.getBalance();
            newBalanceLabel.setText(String.format("%.2f", client.getBalance()));
        } else {
            showAlert("Ошибка", "Информация о клиенте не найдена.");
        }
    }

    @FXML
    void addBalance(ActionEvent event) {
        try {
            // Получаем введенную сумму
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showAlert("Ошибка", "Пожалуйста, введите сумму");
                return;
            }

            amountText = amountText.replace(',', '.');

            // Парсим сумму и проверяем валидность
            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    showAlert("Ошибка", "Сумма должна быть больше нуля");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Ошибка", "Некорректный формат суммы");
                return;
            }
            if (paymentMethodComboBox.getValue() == null) {
                showAlert("Ошибка", "Выберите способ оплаты");
                return;
            }

            // Получаем текущий баланс
            String balanceText = newBalanceLabel.getText().replace(',', '.');
            double currentBalance = Double.parseDouble(balanceText);
            double newBalance = currentBalance + amount;

            // Получаем ID пользователя
            int userId = ConnectDB.id;
            if (userId == 0) {
                showAlert("Ошибка", "Не удалось получить данные пользователя");
                return;
            }

            // Отправляем запрос на сервер
            ConnectDB.user.sendMessage("update_user_balance");

            // Создаем массив с данными для отправки
            Object[] requestData = new Object[]{userId, newBalance};
            ConnectDB.user.sendObject(requestData);

            // Получаем ответ от сервера
            String response = (String) ConnectDB.user.readObject();

            if ("OK".equals(response)) {
                // Обновляем баланс в интерфейсе (форматируем вывод)
                newBalanceLabel.setText(String.format("%.2f", newBalance).replace('.', ','));
                // Обновляем баланс в ConnectDB (если нужно)
                ConnectDB.balance = newBalance;

                showAlert("Успех", "Баланс успешно пополнен на " + amount);
                amountField.clear();
            } else {
                showAlert("Ошибка", response);
            }

        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка при обработке данных");
            e.printStackTrace();
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/windowClient.fxml"));
            Parent registerPage = loader.load();
            Scene registerScene = new Scene(registerPage);
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


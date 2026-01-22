package Controllers.Admin;

import POJO.Tariff;
import ServerWORK.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class TariffsManagementController {

    @FXML
    private VBox mainContainer;
    @FXML private TableView<Tariff> tariffsTable;
    @FXML private TableColumn<Tariff, Integer> idColumn;
    @FXML private TableColumn<Tariff, String> nameColumn;
    @FXML private TableColumn<Tariff, Double> priceColumn;

    @FXML private Button refreshButton;
    @FXML private Button editButton;
    @FXML private Button backButton;
    @FXML private Label statusLabel;

    private ObservableList<Tariff> tariffsData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("tariffId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerHour"));

        loadTariffs();
    }

    private void loadTariffs() {
        try {
            ConnectDB.user.sendMessage("get_all_tariffs");
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                tariffsData.setAll((List<Tariff>) response);
                tariffsTable.setItems(tariffsData);
                statusLabel.setText("Загружено тарифов: " + tariffsData.size());
            } else {
                statusLabel.setText("Ошибка загрузки данных");
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить тарифы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadTariffs();
    }

    @FXML
    private void handleEditTariff() {
        Tariff selected = tariffsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите тариф для редактирования");
            return;
        }
        showTariffDialog(selected);
    }

    private void showTariffDialog(Tariff tariff) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tariffEditDialog.fxml"));
            Parent root = loader.load();

            TariffEditDialogController controller = loader.getController();
            controller.setTariff(tariff);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Изменение тарифа");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();

            if (controller.isSaved()) {
                loadTariffs();
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось открыть диалог: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack()
    {
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            // Загружаем новую сцену
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menuAdmin.fxml"));
            Parent root = loader.load();

            // Создаем новую сцену
            Scene scene = new Scene(root, 800, 600);

            currentStage.setScene(scene);
            currentStage.setTitle("Админ панель");
            currentStage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить главное меню: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
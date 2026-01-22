package Controllers.Сlient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ViewInfoController {
    private final String filePath = "info.xml"; // Путь к XML-файлу

    @FXML
    private VBox mainContainer;
    @FXML
    private TextArea infoTextArea;
    @FXML
    private Label InstaLabel;
    @FXML
    private Label VkLabel;
    @FXML
    private Label mapLabel;


    @FXML
    private void initialize() {
        loadInfo();
        mapLabel.setStyle("-fx-text-fill: white; -fx-underline: false;");
        mapLabel.setOnMouseClicked(this::handleMapClick);
        VkLabel.setStyle("-fx-text-fill: white; -fx-underline: false;");
        VkLabel.setOnMouseClicked(this::handleVkClick);
        InstaLabel.setStyle("-fx-text-fill: white; -fx-underline: false;");
        InstaLabel.setOnMouseClicked(this::handleInstaClick);
    }
    @FXML //Открытие карты по нажатию на ссылку
    private void handleMapClick(MouseEvent event) {
        String url = "https://yandex.by/maps/157/minsk/search/%D0%9A%D0%BE%D0%BC%D0%BF%D1%8C%D1%8E%D1%82%D0%B5%D1%80%D0%BD%D1%8B%D0%B5%20%D0%BA%D0%BB%D1%83%D0%B1%D1%8B/?ll=27.564453%2C53.893008&sll=27.579756%2C53.899853&sspn=0.347149%2C0.124823&z=11.84";
        try {
            Desktop.getDesktop().browse(new URI(url));}
        catch (IOException | URISyntaxException e) {e.printStackTrace();}
    }

    @FXML //Открытие карты по нажатию на ссылку
    private void handleVkClick(MouseEvent event) {
        String url = "https://vk.com/metaarenaminsk?ysclid=mavi60zs3l97933500";
        try {
            Desktop.getDesktop().browse(new URI(url));}
        catch (IOException | URISyntaxException e) {e.printStackTrace();}
    }

    @FXML //Открытие карты по нажатию на ссылку
    private void handleInstaClick(MouseEvent event) {
        String url = "https://www.instagram.com/metaarenaminsk/";
        try {
            Desktop.getDesktop().browse(new URI(url));}
        catch (IOException | URISyntaxException e) {e.printStackTrace();}
    }

    private void loadInfo() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                infoTextArea.setText("Информация не найдена. Добавьте данные в файл.");
                return;
            }

            // Чтение XML-файла
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            // Извлечение данных из XML
            String name = getTextContent(doc, "Name");
            String text = getTextContent(doc, "Text");
            String phone = getTextContent(doc, "Phone");

            // Формирование текста для отображения
            String info = String.format(
                    "Label: %s%n%n%n" +
                            "Info: %s%n%n%n" +
                            "Phone: %s%n%n%n",
                    name, text, phone
            );
            infoTextArea.setText(info);

        } catch (Exception e) {
            infoTextArea.setText("Произошла ошибка при загрузке данных.");
            e.printStackTrace();
        }
    }

    private String getTextContent(Document doc, String tagName) {
        Element element = (Element) doc.getElementsByTagName(tagName).item(0);
        return element != null ? element.getTextContent() : "Не указано";
    }

    @FXML
    private void handleBack()
    {
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

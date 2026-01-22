package Controllers.Сlient;

import POJO.*;
import ServerWORK.ConnectDB;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddClientOrderController {
    @FXML
    private VBox mainContainer;
    @FXML private ComboBox<Computer> masterComboBox;
    @FXML private ComboBox<Tariff> serviceComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private Label PriceLabel;

    private double clientDiscount = 0.0;
    private int clientId;
    private double clientBalance;
    private List<Computer> computers;
    private List<Tariff> tariffs;
    private ObservableList<String> availableTimes = FXCollections.observableArrayList();

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(22, 0);
    private static final int SESSION_DURATION = 60; // в минутах

    @FXML
    public void initialize() {
        if (!initializeClientData()) {
            showAlertAndClose("Ошибка инициализации", "Не удалось загрузить данные клиента");
            return;
        }
        loadClientDiscount();
        initializeUIComponents();
    }

    private void loadClientDiscount() {
        try {
            ConnectDB.user.sendMessage("get_client_discount");
            ConnectDB.user.sendObject(this.clientId);

            Object status = ConnectDB.user.readObject();
            if ("OK".equals(status)) {
                this.clientDiscount = (Double) ConnectDB.user.readObject();
                System.out.println("Текущая скидка клиента: " + clientDiscount + "%");

                // Обновляем отображение скидки
                Platform.runLater(() -> {
                    if (clientDiscount > 0) {
                        PriceLabel.setText("Скидка: " + clientDiscount + "%");
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки скидки:");
            e.printStackTrace();
        }
    }


    private boolean initializeClientData() {
        try {
            // 1. Получаем clientId по userId
            this.clientId = ConnectDB.id;
            if (this.clientId <= 0) {
                System.err.println("Неверный clientId: " + this.clientId);
                return false;
            }

            // 2. Получаем полную информацию о клиенте
            ClientInfo clientInfo = getClientInfoFromServer(this.clientId);
            if (clientInfo == null) {
                System.err.println("Не удалось получить информацию о клиенте");
                return false;
            }

            this.clientBalance = clientInfo.getBalance();
            System.out.printf("Данные клиента загружены: clientId=%d, balance=%.2f%n",
                    clientId, clientBalance);
            return true;

        } catch (Exception e) {
            System.err.println("Ошибка инициализации данных клиента:");
            e.printStackTrace();
            return false;
        }
    }

    private ClientInfo getClientInfoFromServer(int clientId) throws Exception {
        System.out.println("Запрос информации о клиенте для clientId: " + clientId);
        ConnectDB.user.sendMessage("viewClientInfo");
        ConnectDB.user.sendObject(clientId);

        Object response = ConnectDB.user.readObject();
        if (response instanceof ClientInfo) {
            return (ClientInfo) response;
        }
        throw new Exception("Неверный формат ClientInfo: " + (response != null ? response.getClass() : "null"));
    }

    private void initializeUIComponents() {
        try {
            loadComputers();
            loadTariffs();
            setupTimeSlots();
            setupChangeListeners();
        } catch (Exception e) {
            System.err.println("Ошибка инициализации UI:");
            e.printStackTrace();
            throw new RuntimeException("UI initialization failed", e);
        }
    }

    private void showAlertAndClose(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
        Platform.runLater(this::handleBack);
    }

    private void loadComputers() {
        try {
            ConnectDB.user.sendMessage("get_all_computers");
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                computers = (List<Computer>) response;
                displayComputers(computers);
            } else {
                System.out.println("Ошибка при получении компьютеров: " + response);
                showAlert("Ошибка", "Не удалось загрузить список компьютеров", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить список компьютеров", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void displayComputers(List<Computer> computers) {
        ObservableList<Computer> computerList = FXCollections.observableArrayList(computers);
        masterComboBox.setItems(computerList);

        masterComboBox.setCellFactory(param -> new ListCell<Computer>() {
            @Override
            protected void updateItem(Computer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Компьютер №" + item.getComputerId() + " (" + item.getAlive() + ")");
                }
            }
        });

        masterComboBox.setButtonCell(new ListCell<Computer>() {
            @Override
            protected void updateItem(Computer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Компьютер №" + item.getComputerId());
                }
            }
        });
    }

    private void loadTariffs() {
        try {
            ConnectDB.user.sendMessage("get_all_tariffs");
            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                tariffs = (List<Tariff>) response;
                displayTariffs(tariffs);
            } else {
                System.out.println("Ошибка при получении тарифов: " + response);
                showAlert("Ошибка", "Не удалось загрузить список тарифов", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить список тарифов", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void displayTariffs(List<Tariff> tariffs) {
        ObservableList<Tariff> tariffList = FXCollections.observableArrayList(tariffs);
        serviceComboBox.setItems(tariffList);

        serviceComboBox.setCellFactory(param -> new ListCell<Tariff>() {
            @Override
            protected void updateItem(Tariff item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getPricePerHour() + " руб/час");
                }
            }
        });

        serviceComboBox.setButtonCell(new ListCell<Tariff>() {
            @Override
            protected void updateItem(Tariff item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void setupTimeSlots() {
        // Создаем список временных слотов с интервалом в 1 час
        LocalTime time = START_TIME;
        while (time.isBefore(END_TIME)) {
            availableTimes.add(time.format(TIME_FORMATTER));
            time = time.plusMinutes(SESSION_DURATION);
        }
        timeComboBox.setItems(availableTimes);
    }

    private void setupChangeListeners() {
        // При изменении даты или компьютера обновляем доступное время
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableTimes());
        masterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableTimes());

        // При изменении тарифа обновляем цену
        serviceComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());
        timeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());
    }

    private void updateAvailableTimes() {
        if (datePicker.getValue() == null || masterComboBox.getValue() == null) {
            return;
        }

        LocalDate selectedDate = datePicker.getValue();
        int computerId = masterComboBox.getValue().getComputerId();

        try {
            ConnectDB.user.sendMessage("get_computer_bookings");
            ConnectDB.user.sendObject(new Object[]{computerId, selectedDate});

            Object response = ConnectDB.user.readObject();

            if (response instanceof List<?>) {
                List<Booking> bookings = (List<Booking>) response;

                // Фильтруем доступные времена
                List<String> availableSlots = availableTimes.stream()
                        .filter(timeSlot -> isTimeSlotAvailable(timeSlot, bookings))
                        .collect(Collectors.toList());

                // Обновляем ComboBox
                timeComboBox.setItems(FXCollections.observableArrayList(availableSlots));
            } else {
                System.out.println("Ошибка при получении бронирований: " + response);
            }
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось проверить доступность времени", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean isTimeSlotAvailable(String timeSlot, List<Booking> bookings) {
        LocalTime slotTime = LocalTime.parse(timeSlot, TIME_FORMATTER);
        LocalDateTime slotDateTime = LocalDateTime.of(datePicker.getValue(), slotTime);
        LocalDateTime slotEndDateTime = slotDateTime.plusMinutes(SESSION_DURATION);

        for (Booking booking : bookings) {
            if ((slotDateTime.isAfter(booking.getStartTime()) && slotDateTime.isBefore(booking.getEndTime())) ||
                    (slotEndDateTime.isAfter(booking.getStartTime()) && slotEndDateTime.isBefore(booking.getEndTime())) ||
                    (slotDateTime.isBefore(booking.getStartTime()) && slotEndDateTime.isAfter(booking.getEndTime()))) {
                return false;
            }
        }
        return true;
    }

    private void updatePrice() {
        if (serviceComboBox.getValue() == null || timeComboBox.getValue() == null) {
            PriceLabel.setText("Сумма списания: -");
            return;
        }

        double pricePerHour = serviceComboBox.getValue().getPricePerHour();
        double totalPrice = pricePerHour * (SESSION_DURATION / 60.0);

        // Применяем максимальную скидку
        if (clientDiscount > 0) {
            double discountAmount = totalPrice * (clientDiscount / 100);
            totalPrice -= discountAmount;

            PriceLabel.setText(String.format("Сумма списания: %.2f руб. (скидка %.0f%% = %.2f руб.)",
                    totalPrice, clientDiscount, discountAmount));
        } else {
            PriceLabel.setText(String.format("Сумма списания: %.2f руб.", totalPrice));
        }
    }

    @FXML
    private void saveAppointment() {
        if (!validateInput()) {
            return;
        }

        double bookingPrice = calculateBookingPrice();

        // Проверяем баланс с учетом скидки
        if (clientBalance < bookingPrice) {
            showAlert("Ошибка", "Недостаточно средств на балансе", Alert.AlertType.ERROR);
            return;
        }

        // Подтверждение бронирования
        Optional<ButtonType> result = showConfirmation("Подтверждение",
                "Вы уверены, что хотите забронировать компьютер?",
                getBookingDetailsWithDiscount());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // 1. Создаем бронирование
                Booking booking = createBookingFromInput();

                // 2. Отправляем запрос на создание бронирования
                ConnectDB.user.sendMessage("create_booking");
                ConnectDB.user.sendObject(booking);

                Object bookingResponse = ConnectDB.user.readObject();

                if ("OK".equals(bookingResponse)) {
                    // 3. Обновляем баланс локально
                    clientBalance -= bookingPrice;

                    // 4. Отправляем запрос на обновление баланса
                    updateBalanceOnServer(ConnectDB.id, clientBalance);

                    showAlert("Успех", "Бронирование создано!\nНовый баланс: " + clientBalance + " руб.",
                            Alert.AlertType.INFORMATION);
                    handleBack();
                } else {
                    showAlert("Ошибка", "Не удалось создать бронирование", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                showAlert("Ошибка", "Ошибка при бронировании: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void updateBalanceOnServer(int userId, double newBalance) {
        try {
            ConnectDB.user.sendMessage("update_user_balance");
            // Отправляем userId и новый баланс
            ConnectDB.user.sendObject(new Object[]{userId, newBalance});

            Object response = ConnectDB.user.readObject();
            if (!"OK".equals(response)) {
                System.err.println("Ошибка обновления баланса: " + response);
                // Можно добавить повторную попытку или уведомление пользователя
            }
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении баланса:");
            e.printStackTrace();
            // Важно: нужно обработать ситуацию, когда баланс на сервере не обновился
            showAlert("Внимание",
                    "Бронирование создано, но баланс не обновлен. Пожалуйста, проверьте баланс.",
                    Alert.AlertType.WARNING);
        }
    }

    private double calculateBookingPrice() {
        if (serviceComboBox.getValue() == null) return 0;
        double basePrice = serviceComboBox.getValue().getPricePerHour() * (SESSION_DURATION / 60.0);

        // Применяем скидку
        if (clientDiscount > 0) {
            return basePrice * (1 - clientDiscount / 100);
        }
        return basePrice;
    }

    private boolean validateInput() {
        if (masterComboBox.getValue() == null) {
            showAlert("Ошибка", "Выберите компьютер", Alert.AlertType.ERROR);
            return false;
        }

        if (serviceComboBox.getValue() == null) {
            showAlert("Ошибка", "Выберите тариф", Alert.AlertType.ERROR);
            return false;
        }

        if (datePicker.getValue() == null) {
            showAlert("Ошибка", "Выберите дату", Alert.AlertType.ERROR);
            return false;
        }

        if (timeComboBox.getValue() == null) {
            showAlert("Ошибка", "Выберите время", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private Booking createBookingFromInput() {
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.parse(timeComboBox.getValue(), TIME_FORMATTER);
        LocalDateTime startTime = LocalDateTime.of(date, time);
        LocalDateTime endTime = startTime.plusMinutes(SESSION_DURATION);

        Booking booking = new Booking();
        booking.setClientId(clientId);
        booking.setComputerId(masterComboBox.getValue().getComputerId());
        booking.setTariffId(serviceComboBox.getValue().getTariffId());
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setTotalPrice(serviceComboBox.getValue().getPricePerHour() * (SESSION_DURATION / 60.0));
        booking.setStatus("pending"); // Используем одно из допустимых значений
        booking.setCreatedAt(LocalDateTime.now());

        return booking;
    }
    private String getBookingDetailsWithDiscount() {
        double basePrice = serviceComboBox.getValue().getPricePerHour() * (SESSION_DURATION / 60.0);
        double finalPrice = clientDiscount > 0 ?
                basePrice * (1 - clientDiscount / 100) : basePrice;

        return String.format(
                "Детали бронирования:\n\n" +
                        "Компьютер: №%d\n" +
                        "Тариф: %s (%.2f руб/час)\n" +
                        "Дата: %s\n" +
                        "Время: %s - %s\n" +
                        "Продолжительность: %d мин.\n\n" +
                        "Баланс: %.2f руб.\n" +
                        "Стоимость: %.2f руб.%s\n" +
                        "Итоговая сумма: %.2f руб.\n\n" +
                        "Остаток после списания: %.2f руб.",
                masterComboBox.getValue().getComputerId(),
                serviceComboBox.getValue().getName(),
                serviceComboBox.getValue().getPricePerHour(),
                datePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                timeComboBox.getValue(),
                LocalTime.parse(timeComboBox.getValue(), TIME_FORMATTER)
                        .plusMinutes(SESSION_DURATION).format(TIME_FORMATTER),
                SESSION_DURATION,
                clientBalance,
                basePrice,
                clientDiscount > 0 ? String.format("\nСкидка %.0f%%: -%.2f руб.",
                        clientDiscount, basePrice * (clientDiscount / 100)) : "",
                finalPrice,
                clientBalance - finalPrice
        );
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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
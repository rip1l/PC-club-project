package Server;

import Database.*;
import POJO.*;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class ClientHandler implements Runnable {
    protected Socket clientSocket = null;
    ObjectInputStream inpStr;
    ObjectOutputStream outStr;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            inpStr = new ObjectInputStream(clientSocket.getInputStream());
            outStr = new ObjectOutputStream(clientSocket.getOutputStream());
            while (true) {
                String choice = inpStr.readObject().toString();
                System.out.println(choice);
                System.out.println("Команда получена");
                switch (choice) {
                    case "registrationClient" -> {
                        System.out.println("Запрос к БД на регистрацию, клиент: " + clientSocket.getInetAddress().toString());
                        Users users = (Users) inpStr.readObject();
                        System.out.println(users.toString());

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            boolean isUserExists = sqlFactory.getUsers().isUserExists(users.getLogin());

                            if (isUserExists) {
                                outStr.writeObject("This user already exists");
                            } else {
                                boolean isRegistered = sqlFactory.getUsers().insert(users);

                                if (isRegistered) {
                                    outStr.writeObject("OK");
                                } else {
                                    outStr.writeObject("Error during registration");
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            outStr.writeObject("Database error occurred");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Class not found error");
                        }
                        break;
                    }
                    case "authorization" -> {
                        System.out.println("Выполняется авторизация пользователя....");
                        Auth auth = (Auth) inpStr.readObject();
                        System.out.println(auth.toString());

                        try {
                            FactoryDB sqlFactory1 = new FactoryDB();
                            AuthDB authDB = sqlFactory1.getRole();

                            Users user = authDB.getRole(auth);

                            if (user != null) {
                                outStr.writeObject("OK");
                                outStr.writeObject(user);
                            } else {
                                outStr.writeObject("There is no data!");
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Error occurred during authorization.");
                        }
                        break;
                    }
                    case "create_booking" -> {
                        System.out.println("Создание бронирования...");
                        Booking booking = (Booking) inpStr.readObject();

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntBookingDB bookingDB = sqlFactory.getBookingDB();

                            boolean success = bookingDB.createBooking(booking);
                            outStr.writeObject(success ? "OK" : "Failed to create booking");

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "get_all_bookings" -> {
                        System.out.println("Получение списка бронирований...");
                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntBookingDB bookingDB = sqlFactory.getBookingDB();

                            List<Booking> bookings = bookingDB.getAllBookings();
                            outStr.writeObject(bookings);

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "cancel_booking" -> {
                        System.out.println("Отмена бронирования...");
                        int bookingId = (int) inpStr.readObject();

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntBookingDB bookingDB = sqlFactory.getBookingDB();

                            boolean success = bookingDB.cancelBooking(bookingId);
                            outStr.writeObject(success ? "OK" : "Failed to cancel booking");

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "get_client_bookings" -> {
                        System.out.println("Получение бронирований клиента...");
                        int clientId = (int) inpStr.readObject();

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntBookingDB bookingDB = sqlFactory.getBookingDB();

                            List<Booking> bookings = bookingDB.getClientBookings(clientId);
                            outStr.writeObject(bookings.isEmpty() ? "No bookings found" : "OK");
                            outStr.writeObject(bookings);

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "get_computer_bookings" -> {
                        System.out.println("Получение бронирований компьютера...");
                        Object[] data = (Object[]) inpStr.readObject();
                        int computerId = (int) data[0];
                        LocalDate date = (LocalDate) data[1];

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntBookingDB bookingDB = sqlFactory.getBookingDB();

                            List<Booking> bookings = bookingDB.getComputerBookings(computerId, date);
                            outStr.writeObject(bookings);

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "check_availability" -> {
                        System.out.println("Проверка доступности компьютера...");
                        Object[] data = (Object[]) inpStr.readObject();
                        int computerId = (int) data[0];
                        LocalDateTime start = (LocalDateTime) data[1];
                        LocalDateTime end = (LocalDateTime) data[2];

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntBookingDB bookingDB = sqlFactory.getBookingDB();

                            boolean isAvailable = bookingDB.checkComputerAvailability(computerId, start, end);
                            outStr.writeObject(isAvailable ? "OK" : "Computer is booked");

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "get_all_computers" -> {
                        System.out.println("Получение списка компьютеров...");

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntComputerDB computerDB = sqlFactory.getComputerDB();

                            List<Computer> computers = computerDB.getAllComputers();
                            outStr.writeObject(computers);

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "get_computer_by_id" -> {
                        System.out.println("Получение компьютера по ID...");
                        int computerId = (int) inpStr.readObject();

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntComputerDB computerDB = sqlFactory.getComputerDB();

                            Computer computer = computerDB.getComputerById(computerId);
                            outStr.writeObject(computer == null ? "Computer not found" : "OK");
                            outStr.writeObject(computer);

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "update_computer_status" -> {
                        System.out.println("Обновление статуса компьютера...");
                        Object[] data = (Object[]) inpStr.readObject();
                        int computerId = (int) data[0];
                        boolean isActive = (boolean) data[1];

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntComputerDB computerDB = sqlFactory.getComputerDB();

                            boolean success = computerDB.updateComputerStatus(computerId, isActive);
                            outStr.writeObject(success ? "OK" : "Failed to update status");

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "add_client_to_group" -> {
                        System.out.println("Добавление клиента в группу...");
                        Object[] data = (Object[]) inpStr.readObject();
                        int clientId = (int) data[0];
                        int groupId = (int) data[1];

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntGroupDB groupDB = sqlFactory.getGroupDB();

                            boolean success = groupDB.addClientToGroup(clientId, groupId);
                            outStr.writeObject(success ? "OK" : "Failed to add client to group");

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "remove_client_from_group" -> {
                        System.out.println("Удаление клиента из группы...");
                        Object[] data = (Object[]) inpStr.readObject();
                        int clientId = (int) data[0];
                        int groupId = (int) data[1];

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntGroupDB groupDB = sqlFactory.getGroupDB();

                            boolean success = groupDB.removeClientFromGroup(clientId, groupId);
                            outStr.writeObject(success ? "OK" : "Failed to remove client from group");

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "get_all_groups" -> {
                        System.out.println("Получение списка групп...");
                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntGroupDB groupDB = sqlFactory.getGroupDB();
                            List<ClientGroup> groups = groupDB.getAllGroups();

                            // Отправляем сразу список, без предварительного "OK"
                            outStr.writeObject(groups);
                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject(new ArrayList<ClientGroup>()); // Возвращаем пустой список при ошибке
                        }
                        break;
                    }

                    case "get_client_discount" -> {
                        System.out.println("Получение скидки клиента...");
                        int clientId = (int) inpStr.readObject();

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntGroupDB groupDB = sqlFactory.getGroupDB();

                            double discount = groupDB.getClientDiscount(clientId);
                            outStr.writeObject("OK");
                            outStr.writeObject(discount);

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "get_all_tariffs" -> {
                        System.out.println("Получение списка тарифов...");

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntTariffDB tariffDB = sqlFactory.getTariffDB();

                            List<Tariff> tariffs = tariffDB.getAllTariffs();
                            outStr.writeObject(tariffs);
                            System.out.println("Отправлено тарифов: " + tariffs.size());

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "get_tariff_by_id" -> {
                        System.out.println("Получение тарифа по ID...");
                        int tariffId = (int) inpStr.readObject();

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntTariffDB tariffDB = sqlFactory.getTariffDB();

                            Tariff tariff = tariffDB.getTariffById(tariffId);
                            outStr.writeObject(tariff == null ? "Tariff not found" : "OK");
                            outStr.writeObject(tariff);

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "update_tariff" -> {
                        System.out.println("Обновление тарифа...");
                        Tariff tariff = (Tariff) inpStr.readObject();

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntTariffDB tariffDB = sqlFactory.getTariffDB();

                            boolean success = tariffDB.updateTariff(tariff);
                            outStr.writeObject(success ? "OK" : "Failed to update tariff");

                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "update_user_balance" -> {
                        System.out.println("[Server] Обновление баланса пользователя...");

                        // Чтение данных от клиента: ID пользователя и новая сумма баланса
                        Object[] requestData = (Object[]) inpStr.readObject();
                        int userId = (int) requestData[0];
                        double newBalance = (double) requestData[1];

                        try {
                            // Получение экземпляра UsersDB через фабрику
                            FactoryDB factory = new FactoryDB();
                            IntUsersDB usersDB = factory.getUsers();

                            // Вызов метода обновления баланса
                            boolean isUpdated = usersDB.updateUserBalance(userId, newBalance);

                            // Отправка результата клиенту
                            if (isUpdated) {
                                outStr.writeObject("OK");  // Успех
                            } else {
                                outStr.writeObject("Ошибка: Не удалось обновить баланс");
                            }

                        } catch (SQLException | ClassNotFoundException e) {
                            // Обработка ошибок БД
                            outStr.writeObject("Ошибка сервера: " + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    }

                    case "addClientInfo" -> {
                        System.out.println("Запрос на добавление информации о клиенте от: " + clientSocket.getInetAddress().toString());

                        Clients client = (Clients) inpStr.readObject();
                        System.out.println(client.toString());

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            boolean isClientExists = sqlFactory.getClientsDB().isClientInfoExists(client.getUserId());

                            if (isClientExists) {
                                outStr.writeObject("Client information already exists");
                            } else {
                                boolean isAdded = sqlFactory.getClientsDB().insert(client);

                                if (isAdded) {
                                    outStr.writeObject("Client information added successfully");
                                } else {
                                    outStr.writeObject("Error during adding client information");
                                }
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Database error occurred while adding client information");
                        }
                        break;
                    }

                    case "checkClientExists" -> {
                        System.out.println("Запрос на проверку существования клиента от: " + clientSocket.getInetAddress().toString());

                        try {
                            int clientId = (int) inpStr.readObject();
                            System.out.println("Проверка клиента с ID: " + clientId);

                            FactoryDB sqlFactory = new FactoryDB();

                            boolean isClientExists = sqlFactory.getClientsDB().isClientInfoExists(clientId);

                            if (isClientExists) {
                                outStr.writeObject("Client exists");
                            } else {
                                outStr.writeObject("Client does not exist");
                            }
                        } catch (SQLException | ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                            try {
                                outStr.writeObject("Error during client existence check");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                        break;
                    }
                    case "viewClientInfoForShow" -> {
                        System.out.println("Запрос на просмотр информации о клиенте от: " + clientSocket.getInetAddress().toString());
                        int userId = (int) inpStr.readObject();

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            // Получаем данные клиента
                            Clients client = sqlFactory.getClientsDB().getClientByUserId(userId);

                            // Получаем баланс пользователя
                            double balance = sqlFactory.getUsers().getBalanceByUserId(userId);

                            if (client != null) {
                                ClientInfo clientInfo = new ClientInfo(client, balance);
                                outStr.writeObject(clientInfo);
                            } else {
                                outStr.writeObject(null);
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject(null);
                        }
                        break;
                    }

                    case "viewClientInfo" -> {
                        System.out.println("Запрос на просмотр информации о клиенте от: " + clientSocket.getInetAddress().toString());
                        int userId = (int) inpStr.readObject();
                        System.out.println("Ищем клиента с userId: " + userId);

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            // Получаем данные клиента
                            Clients client = sqlFactory.getClientsDB().getClientByUserId(userId);

                            if (client == null) {
                                System.out.println("Клиент с userId=" + userId + " не найден");
                                outStr.writeObject("CLIENT_NOT_FOUND");
                                break;
                            }

                            // Получаем баланс пользователя
                            double balance = sqlFactory.getUsers().getBalanceByUserId(userId); // Предполагаем, что у вас есть такой метод
                            System.out.println("Найден клиент: ID=" + client.getClientId() + ", баланс=" + balance);
                            if (client != null) {
                                // Создаем объект с информацией о клиенте и балансе
                                ClientInfo clientInfo = new ClientInfo(client, balance);
                                outStr.writeObject(clientInfo);
                            } else {
                                outStr.writeObject(null);
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject(null);
                        }
                        break;
                    }

                    case "updateClientInfo" -> {
                        System.out.println("Запрос на обновление информации от клиента: " + clientSocket.getInetAddress().toString());

                        String newLogin = (String) inpStr.readObject();
                        Clients client = (Clients) inpStr.readObject();

                        System.out.println("Получены данные для обновления: Логин = " + newLogin + ", " + client);

                        FactoryDB sqlFactory = new FactoryDB();

                        try {
                            boolean isLoginUpdated = sqlFactory.getClientsDB().updateLogin(client.getUserId(), newLogin);
                            if (isLoginUpdated) {
                                outStr.writeObject("Логин успешно обновлен.");
                            } else {
                                outStr.writeObject("Ошибка при обновлении логина.");
                            }

                            boolean isInfoUpdated = sqlFactory.getClientsDB().updateClientInfo(
                                    client.getUserId(),
                                    client.getLastName(),
                                    client.getFirstName(),
                                    client.getMiddleName(),
                                    client.getPhoneNumber()
                            );

                            if (isInfoUpdated) {
                                outStr.writeObject("Информация о клиенте успешно обновлена.");
                            } else {
                                outStr.writeObject("Ошибка при обновлении информации о клиенте.");
                            }

                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Ошибка базы данных при обновлении информации.");
                        }

                        break;
                    }

                    case "update_bookings_status" -> {
                        System.out.println("Обновление статусов бронирований...");
                        List<Booking> bookings = (List<Booking>) inpStr.readObject();

                        try {
                            FactoryDB sqlFactory = new FactoryDB();
                            IntBookingDB bookingDB = sqlFactory.getBookingDB();

                            boolean success = true;
                            for (Booking booking : bookings) {
                                if (!bookingDB.updateBookingStatus(booking.getBookingId(), booking.getStatus())) {
                                    success = false;
                                    break;
                                }
                            }

                            outStr.writeObject(success ? "OK" : "Failed to update some bookings");
                        } catch (SQLException | ClassNotFoundException e) {
                            outStr.writeObject("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "addReview" -> {
                        System.out.println("Запрос на добавление отзыва от клиента: " + clientSocket.getInetAddress().toString());

                        Review review = (Review) inpStr.readObject();
                        System.out.println(review.toString());

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            IntReviewDB reviewDB = sqlFactory.getReviewDB();

                            boolean isAdded = reviewDB.addReview(review);

                            if (isAdded) {
                                outStr.writeObject("Review added successfully");
                            } else {
                                outStr.writeObject("Error while adding review");
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Database error occurred while adding review");
                        }
                        break;
                    }

                    case "viewReviews" -> {
                        System.out.println("Запрос на получение всех отзывов от клиента: " + clientSocket.getInetAddress().toString());

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            IntReviewDB reviewDB = sqlFactory.getReviewDB();

                            List<Review> reviews = reviewDB.getAllReviews();

                            outStr.writeObject(reviews);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Error occurred while retrieving reviews");
                        }
                        break;
                    }

                    case "deleteReview" -> {
                        System.out.println("Запрос на удаление отзыва от клиента: " + clientSocket.getInetAddress().toString());
                        int reviewId = (int) inpStr.readObject();
                        System.out.println("Удаление отзыва с ID: " + reviewId);

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            IntReviewDB reviewDB = sqlFactory.getReviewDB();
                            boolean isDeleted = reviewDB.deleteReview(reviewId);

                            if (isDeleted) {
                                outStr.writeObject("Review deleted successfully");
                            } else {
                                outStr.writeObject("Error while deleting review");
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Database error occurred while deleting review");
                        }
                        break;
                    }

                    case "deleteUser" -> {
                        System.out.println("Запрос на удаление пользователя от клиента: " + clientSocket.getInetAddress().toString());
                        String login = (String) inpStr.readObject();
                        System.out.println("Удаление пользователя с логином: " + login);

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            UsersDB usersDB = sqlFactory.getUsers();
                            boolean isDeleted = usersDB.deleteUserByLogin(login);
                            if (isDeleted) {
                                outStr.writeObject("User deleted successfully");
                            } else {
                                outStr.writeObject("Error while deleting user");
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Database error occurred while deleting user");
                        }
                        break;
                    }

                    case "updateUserRole" -> {
                        System.out.println("Запрос на изменение роли пользователя от клиента: " + clientSocket.getInetAddress().toString());
                        String login = (String) inpStr.readObject();
                        String newRole = (String) inpStr.readObject();
                        System.out.println("Изменение роли пользователя с логином: " + login + " на " + newRole);

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            UsersDB usersDB = sqlFactory.getUsers();
                            boolean isUpdated = usersDB.updateUserRoleByLogin(login, newRole);

                            if (isUpdated) {
                                outStr.writeObject("User role updated successfully");
                            } else {
                                outStr.writeObject("Error while updating user role");
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Database error occurred while updating user role");
                        }
                        break;
                    }

                    case "viewAllUsers" -> {
                        System.out.println("Запрос на получение всех пользователей от клиента: " + clientSocket.getInetAddress().toString());

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            UsersDB usersDB = sqlFactory.getUsers();
                            List<Users> usersList = usersDB.getAllUsers();

                            outStr.writeObject(usersList);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Error occurred while retrieving all users");
                        }
                        break;
                    }

                    case "getClientIdByUserId" -> {
                        System.out.println("Запрос на получение client_id от клиента: " + clientSocket.getInetAddress().toString());

                        int usersId = (Integer) inpStr.readObject();

                        FactoryDB sqlFactory = new FactoryDB();
                        try {
                            IntClientsDB clientDB = sqlFactory.getClientsDB();
                            int clientId = clientDB.getClientIdByUserId(usersId);

                            outStr.writeObject(clientId);
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            outStr.writeObject("Error occurred while retrieving client_id");
                        }
                        break;
                    }
                    default -> {
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected.");
        }
    }
}
package POJO;

import java.io.Serializable;

public class Clients implements Serializable {
    private int clientId;       // Идентификатор клиента
    private int userId;         // Внешний ключ из таблицы users
    private String lastName;    // Фамилия клиента
    private String firstName;   // Имя клиента
    private String middleName;  // Отчество клиента (может быть null)
    private String phoneNumber; // Номер телефона клиента

    // Конструктор без параметров (нужен для сериализации/десериализации)
    public Clients() {}

    // Полный конструктор
    public Clients(int clientId, int userId, String lastName, String firstName, String middleName, String phoneNumber) {
        this.clientId = clientId;
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.phoneNumber = phoneNumber;
    }

    // Геттеры и сеттеры
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Метод для вывода информации о клиенте
    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", userId=" + userId +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

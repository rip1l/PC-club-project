package POJO;

import java.io.Serializable;
import java.util.Objects;

public class Users implements Serializable{

    private static final long serialVersionUID = 1L;

    private int id;
    private String login;
    private String password;
    private String role;
    private double balance;

    // Конструктор без параметров
    public Users() {}

    // Конструктор с параметрами
    public Users(int id, String login, String password, String role, double balance) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.balance = balance;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id == users.id &&
                Objects.equals(login, users.login) &&
                Objects.equals(password, users.password) &&
                Objects.equals(role, users.role) &&
                Objects.equals(balance, users.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, balance);
    }

    @Override
    public String toString() {
        return "Users{ login='" + login + "', password='" + password + "', role='" + role + "', balance='" + balance + "}";
    }
}
package POJO;

import java.io.Serializable;

public class Services implements Serializable {
    private int serviseId;
    private String name;
    private String description;
    private double price;
    private String duration;

    // Конструктор без параметров
    public Services() {
    }

    // Конструктор с параметрами
    public Services(int serviseId, String name, String description, double price, String duration) {
        this.serviseId = serviseId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    // Геттеры и сеттеры
    public int getServiseId() {
        return serviseId;
    }

    public void setServiseId(int serviseId) {
        this.serviseId = serviseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    // Переопределение toString для удобства отображения данных
    @Override
    public String toString() {
        return "Services{" +
                "serviseId=" + serviseId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration='" + duration + '\'' +
                '}';
    }
}

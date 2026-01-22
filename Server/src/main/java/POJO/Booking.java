package POJO;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private int bookingId;
    private int clientId;
    private int computerId;
    private int tariffId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;

    public Booking(){}

    public Booking(int bookingId, int clientId, int computerId, int tariffId, LocalDateTime startTime, LocalDateTime endTime, double totalPrice, String status, LocalDateTime createdAt){
        this.bookingId = bookingId;
        this.clientId = clientId;
        this.computerId = computerId;
        this.tariffId = tariffId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getBookingId() {return bookingId;}
    public void setBookingId(int bookingId) {this.bookingId = bookingId;}
    public int getClientId() {return clientId;}
    public void setClientId(int clientId) {this.clientId = clientId;}
    public int getComputerId() {return computerId;}
    public void setComputerId(int computerId) {this.computerId = computerId;}
    public int getTariffId() {return tariffId;}
    public void setTariffId(int tariffId) {this.tariffId = tariffId;}
    public LocalDateTime getStartTime() {return startTime;}
    public void setStartTime (LocalDateTime startTime){this.startTime = startTime;}
    public LocalDateTime getEndTime() {return endTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}
    public double getTotalPrice() {return totalPrice;}
    public void getTotalPrice(double totalPrice) {this.totalPrice = totalPrice;}
    public String getStatus() {return status;}
    public void setStatus(String status) {
        if (status == null || !(status.equals("pending") ||
                status.equals("confirmed") || status.equals("completed"))) {
            throw new IllegalArgumentException("Недопустимый статус бронирования");
        }
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void getCreatedAt (LocalDateTime createdAt) {this.createdAt = createdAt;}

    @Override
    public String toString(){
        return "Booking{" +
                "bookingId=" + bookingId +
                ", clientId=" + clientId +
                ", computerId=" + computerId +
                ", tariffId=" + tariffId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}

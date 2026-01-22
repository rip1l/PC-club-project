package POJO;

import java.io.Serializable;

public class ClientInfo implements Serializable {
    private Clients client;
    private double balance;

    public ClientInfo(Clients client, double balance) {
        this.client = client;
        this.balance = balance;
    }
    public Clients getClient() {
        return client;
    }

    public double getBalance() {
        return balance;
    }
}
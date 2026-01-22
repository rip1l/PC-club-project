package POJO;

import java.io.Serializable;

public class Tariff implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private double pricePerHour;

    public Tariff() {}
    public Tariff(int id, String name, double pricePerHour){
        this.id = id;
        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    public int getTariffId() {return id;}
    public void setTariffId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public double getPricePerHour() {return pricePerHour;}
    public void setPricePerHour(double pricePerHour) {this.pricePerHour = pricePerHour;}

    @Override
    public String toString(){
        return "Tariff{" +
                "tariffId=" + id +
                "tariffName=" + name +
                "pricePerHour=" + pricePerHour +
                '}';
    }
}
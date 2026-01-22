package POJO;

import java.io.Serializable;

public class Computer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String specs;
    private boolean isAlive;

    public Computer(){}

    public Computer(int id, String specs, boolean isAlive){
        this.id = id;
        this.specs = specs;
        this.isAlive = isAlive;
    }

    public int getComputerId() {return id;}
    public void setComputerId(int id) { this.id = id;}
    public String getSpecs() {return specs;}
    public void setSpecs(String specs) {this.specs = specs;}
    public boolean getAlive() {return isAlive;}
    public void setAlive(boolean isAlive) {this.isAlive = isAlive;}

    @Override
    public String toString() {
        return "PC{" +
                "Pc Id=" + id +
                ", Specs=" + specs +
                ", isAlive" + isAlive +
                '}';
    }
}

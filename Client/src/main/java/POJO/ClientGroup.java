package POJO;

import java.io.Serializable;

public class ClientGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private double discountPersent;

    public ClientGroup() {}
    public ClientGroup(int id, String name, double discountPersent){
        this.id =id;
        this.name = name;
        this.discountPersent = discountPersent;
    }

    public int getGroupId() {return id;}
    public void setGroupId(int id) {this.id = id;}
    public String getGroupName() {return name;}
    public void setGroupName(String name) {this.name = name;}
    public double getDiscountPersent() {return discountPersent;}
    public void setDiscountPersent(double discountPersent) {this.discountPersent= discountPersent;}

    @Override
    public String toString(){
        return "Group{" +
                "groupId=" + id +
                "groupName=" + name +
                "discountPersent=" + discountPersent +
                '}';
    }
}
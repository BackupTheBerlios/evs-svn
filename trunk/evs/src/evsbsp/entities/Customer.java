package evsbsp.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {

    private static final long serialVersionUID = 3283743094572853753L;
    
    private int id;
    private String name;
    private String userName;
    private String password;
    private List<Order> orders;

    public Customer () {
        orders = new ArrayList<Order> ();
    }

    public Customer (String name, String userName, String password) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        orders = new ArrayList<Order> ();
    }

    public int getId () {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName () {
        return this.name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getUserName () {
        return this.userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getPassword () {
        return this.password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public List<Order> getOrders () {
        return orders;
    }

    public void addOrder (Order order) {
        orders.add (order);
    }

    public void removeOrder (Order order) {
        orders.remove (order);
    }
}

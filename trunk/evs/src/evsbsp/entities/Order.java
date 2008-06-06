package evsbsp.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {

    private static final long serialVersionUID = -306021298577299922L;
    private Customer customer;
    private List<Product> products;
    
    public Order () {
        products = new ArrayList<Product> ();
    }
    
    public Order (Customer customer) {
        this.customer = customer;
    }
    
    public Customer getCustomer () {
        return this.customer;
    }
    
    public void setCustomer (Customer customer) {
        this.customer = customer;
    }
    
    public List<Product> getProducts () {
        return this.products;
    }
    
    public void addProduct (Product product) {
        products.add (product);
    }
    
    public void removeProduct (Product product) {
        products.remove (product);
    }
}

package evs.app_server;

import evs.entities.Customer;
import evs.entities.Order;
import evs.entities.Product;
import java.util.List;

/**
 *
 * @author Florian Lukavsky
 *         0325558
 */
public class Server implements IServer {

    private static Server server;
    private int customerSequence = 0;
    private int productSequence = 0;
    private List<Customer> customers;
    private List<Product> products;

    public void createCustomer (Customer customer) {
        customer.setId (customerSequence);
        customers.add (customer);
        ++customerSequence;
    }

    public void updateCustomer (Customer customer) {
        Customer cust = findCustomer (customer.getId ());
        if (cust != null) {
            cust.setName (customer.getName ());
            cust.setUserName (customer.getUserName ());
            cust.setPassword (customer.getPassword ());
        }
    }

    private Customer findCustomer (int id) {
        for (Customer customer : customers) {
            if (customer.getId () == id) {
                return customer;
            }
        }
        return null;
    }

    public void createProduct (Product product) {
        product.setId (productSequence);
        products.add (product);
        ++productSequence;
    }

    public void updateProduct (Product product) {
        for (Product prod : products) {
            if (product.getId () == prod.getId ()) {
                prod.setName (product.getName ());
                prod.setPrice (product.getPrice ());
            }
        }
    }

    public Customer login (String userName, String password) {
        for (Customer customer : customers) {
            if (customer.getUserName ().equals (userName) && customer.getPassword ().equals (password)) {
                return customer;
            }
        }
        return null;
    }

    public void buyOrder (Order order) {
        Customer customer = findCustomer (order.getCustomer ().getId ());
        if (customer != null) {
            customer.addOrder (order);
        }
    }
    
    public List<Product> listPorducts () {
        return products;
    }
    
    public static Server getInstance () {
        if (server == null) {
            server = new Server ();
        }
        return server;
    }
}

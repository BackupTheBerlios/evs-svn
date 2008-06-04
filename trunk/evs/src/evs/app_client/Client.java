package evs.app_client;

import evs.app_server.*;
import evs.entities.Customer;
import evs.entities.Order;
import evs.entities.Product;

/**
 *
 * @author Florian Lukavsky
 *         0325558
 */
public class Client {
    
    private IServer server;
    private Customer customer;
    private Order order;
    
    public Client () {
        server = Server.getInstance ();
        order = new Order ();
    }
    
    public boolean login (String userName, String password) {
        customer = server.login (userName, password);
        if (customer != null) {
            order.setCustomer (customer);
            return true;
        }
        return false;
    }
    
    public void addProduct (Product product) {
        order.addProduct (product);
    }

    public void buyOrder () {
        server.buyOrder(order);
        order = new Order ();
    }
}

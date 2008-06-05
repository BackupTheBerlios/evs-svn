package evs.app_server;

import java.util.List;

import evsbsp.entities.Customer;
import evsbsp.entities.Order;
import evsbsp.entities.Product;

/**
 *
 * @author Florian Lukavsky
 *         0325558
 */
public interface IServer {

    void buyOrder (Order order);

    void createCustomer (Customer customer);

    void createProduct (Product product);

    List<Product> listProducts ();

    Customer login (String userName, String password);

    void updateCustomer (Customer customer);

    void updateProduct (Product product);

}

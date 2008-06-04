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
public interface IServer {

    void buyOrder (Order order);

    void createCustomer (Customer customer);

    void createProduct (Product product);

    List<Product> listPorducts ();

    Customer login (String userName, String password);

    void updateCustomer (Customer customer);

    void updateProduct (Product product);

}

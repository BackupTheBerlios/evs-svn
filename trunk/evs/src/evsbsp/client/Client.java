package evsbsp.client;

import evs.exception.NotSupportedException;
import evs.interfaces.IACT;
import evs.interfaces.ICallback;
import evsbsp.IEcommerceOperations;
import evsbsp.entities.Customer;
import evsbsp.entities.Order;
import evsbsp.entities.Product;

/**
 *
 * @author Florian Lukavsky
 *         0325558
 */
public class Client implements ICallback{
    
    private IEcommerceOperations proxy;
    private Customer customer;
    private Order order;
    
    public Client () {
        proxy = new EcommerceProxy(this);
        order = new Order ();
    }
    
    public boolean login (String userName, String password) throws NotSupportedException {
        customer = (Customer) proxy.login(userName, password, null);
        if (customer != null) {
            order.setCustomer (customer);
            return true;
        }
        return false;
    }
    
    public void addProduct (Product product) {
        order.addProduct(product);
    }

    public void buyOrder() throws NotSupportedException {
        proxy.buyOrder(order, null);
        order = new Order ();
    }
    
	public void resultReturned(IACT act, Object result) {
		// TODO Auto-generated method stub
		//TODO
	}
    
}

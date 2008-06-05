package evsbsp;

/* Automatically generated by EVS JavaCodeGenerator */

import evs.interfaces.IACT;
import evs.exception.NotSupportedException;

public interface IEcommerceOperations {
	public Object buyOrder(evsbsp.entities.Order order, IACT act) throws NotSupportedException;
	public Object createCustomer(evsbsp.entities.Customer customer, IACT act) throws NotSupportedException;
	public Object createProduct(evsbsp.entities.Product product, IACT act) throws NotSupportedException;
	public Object listProducts(IACT act) throws NotSupportedException;
	public Object login(String userName, String password, IACT act) throws NotSupportedException;
	public Object updateCustomer(evsbsp.entities.Customer customer, IACT act) throws NotSupportedException;
	public Object updateProduct(evsbsp.entities.Product product, IACT act) throws NotSupportedException;
}
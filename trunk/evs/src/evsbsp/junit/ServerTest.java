package evsbsp.junit;

import evs.core.ACT;
import evs.exception.NotSupportedException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import evsbsp.entities.Customer;
import evsbsp.entities.Product;
import evsbsp.server.Ecommerce;

public class ServerTest {

    Ecommerce server;

    public ServerTest () {
    }

    @Before
    public void setUp () {
        try {
            //TODO: get remote interface
            server = Ecommerce.getInstance ();
            Product product0 = new Product ("Luftmatratze", new BigDecimal ("29.99"));
            Product product1 = new Product ("Quietscheente", new BigDecimal ("9.99"));
            Product product2 = new Product ("Sponge Bob", new BigDecimal ("14.99"));
            server.createProduct (product0, new ACT ());
            server.createProduct (product1, new ACT ());
            server.createProduct (product2, new ACT ());
            Customer customer0 = new Customer ("Gerald Scharitzer", "gerald", "xyz");
            Customer customer1 = new Customer ("Florian Lukavsky", "florian", "flo");
            Customer customer2 = new Customer ("Martina Lindorfer", "martina", "martina1983");
            Customer customer3 = new Customer ("Dirk Wallerstorfer", "dirk", "0626775");
            Customer customer4 = new Customer ("Daniel Priewasser", "daniel", "meinPasswort");
            server.createCustomer (customer0, new ACT ());
            server.createCustomer (customer1, new ACT ());
            server.createCustomer (customer2, new ACT ());
            server.createCustomer (customer3, new ACT ());
            server.createCustomer (customer4, new ACT ());
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        }
    }

    @After
    public void tearDown () {
    }

    @Test
    public void testProducts () {
        try {
            List<Product> products = (List<Product>) server.listProducts (new ACT ());
            Assert.assertEquals (products.get (0).getId (), 0);
            Assert.assertEquals (products.get (2).getId (), 2);
            Assert.assertEquals (products.get (0).getName (), "Luftmatratze");
            Assert.assertEquals (products.get (1).getName (), "Quietscheente");
            Assert.assertEquals (products.get (2).getPrice (), new BigDecimal ("14.99"));
            Product product = products.get (0);
            product.setPrice (new BigDecimal (24.99));
            server.updateProduct (product, new ACT ());
            products = (List<Product>) server.listProducts (new ACT ());
            Assert.assertEquals (product, products.get (0));
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testCustomers () {
        try {
            Customer customer = (Customer) server.login ("martina", "martina1983", new ACT ());
            Assert.assertNotNull (customer);
            Assert.assertEquals (customer.getId (), 2);
            customer.setPassword ("TigerEnte");
            server.updateCustomer (customer, new ACT ());
            customer = (Customer) server.login ("martina", "TigerEnte", new ACT ());
            Assert.assertNotNull (customer);
            customer = (Customer) server.login ("martina", "martina1983", new ACT ());
            Assert.assertNull (customer);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        }
    }
}

package evs.junit;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import evs.app_server.IServer;
import evs.app_server.Server;
import evsbsp.entities.Customer;
import evsbsp.entities.Product;

public class ServerTest {
    
    IServer server;

    public ServerTest() {
    }

    @Before
    public void setUp() {
        //TODO: get remote interface
        server = Server.getInstance ();
        Product product0 = new Product ("Luftmatratze", new BigDecimal ("29.99"));
        Product product1 = new Product ("Quietscheente", new BigDecimal ("9.99"));
        Product product2 = new Product ("Sponge Bob", new BigDecimal ("14.99"));
        server.createProduct (product0);
        server.createProduct (product1);
        server.createProduct (product2);
        Customer customer0 = new Customer ("Gerald Scharitzer", "gerald", "xyz");
        Customer customer1 = new Customer ("Florian Lukavsky", "florian", "flo");
        Customer customer2 = new Customer ("Martina Lindorfer", "martina", "martina1983");
        Customer customer3 = new Customer ("Dirk Wallerstorfer", "dirk", "0626775");
        Customer customer4 = new Customer ("Daniel Priewasser", "daniel", "meinPasswort");
        server.createCustomer (customer0);
        server.createCustomer (customer1);
        server.createCustomer (customer2);
        server.createCustomer (customer3);
        server.createCustomer (customer4);
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testProducts () {
        List<Product> products = server.listProducts ();
        Assert.assertEquals (products.get (0).getId (), 0);
        Assert.assertEquals (products.get (2).getId (), 2);
        Assert.assertEquals (products.get (0).getName (), "Luftmatratze");
        Assert.assertEquals (products.get (1).getName (), "Quietscheente");
        Assert.assertEquals (products.get (2).getPrice (), new BigDecimal ("14.99"));
        Product product = products.get (0);
        product.setPrice (new BigDecimal (24.99));
        server.updateProduct (product);
        products = server.listProducts ();
        Assert.assertEquals (product, products.get (0));
    }
    
    @Test
    public void testCustomers () {
        Customer customer = server.login ("martina", "martina1983");
        Assert.assertNotNull (customer);
        Assert.assertEquals(customer.getId (), 2);
        customer.setPassword ("TigerEnte");
        server.updateCustomer (customer);
        customer = server.login ("martina", "TigerEnte");
        Assert.assertNotNull (customer);
        customer = server.login ("martina", "martina1983");
        Assert.assertNull (customer);
    }

}
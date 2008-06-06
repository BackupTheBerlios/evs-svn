package evsbsp.junit;

import evs.core.ACT;
import evs.core.InvocationObject;
import evs.exception.NotSupportedException;
import evs.main.Peer;
import evsbsp.IEcommerceOperations;
import evsbsp.client.Client;

import evsbsp.entities.Customer;
import evsbsp.entities.Order;
import evsbsp.entities.Product;
import java.math.BigDecimal;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {

    Client client;
    Peer peer;
    IEcommerceOperations proxy;

    public ClientTest () {
    }

    @Before
    public void setUp () {
        String[] args = {};
        peer = new Peer (args);
        peer.run ();
        peer.processCommand ("register-object=evsbsp.server.Ecommerce");
        peer.processCommand ("port=31337");
        peer.processCommand ("listen");
        client = new Client ();
        proxy = client.getProxy ();
        try {
            Product product0 = new Product ("Luftmatratze", new BigDecimal ("29.99"));
            Product product1 = new Product ("Quietscheente", new BigDecimal ("9.99"));
            Product product2 = new Product ("Sponge Bob", new BigDecimal ("14.99"));
            proxy.createProduct (product0, new ACT ());
            proxy.createProduct (product1, new ACT ());
            proxy.createProduct (product2, new ACT ());
            Customer customer0 = new Customer ("Gerald Scharitzer", "gerald", "xyz");
            Customer customer1 = new Customer ("Florian Lukavsky", "florian", "flo");
            Customer customer2 = new Customer ("Martina Lindorfer", "martina", "martina1983");
            Customer customer3 = new Customer ("Dirk Wallerstorfer", "dirk", "0626775");
            Customer customer4 = new Customer ("Daniel Priewasser", "daniel", "meinPasswort");
            proxy.createCustomer (customer0, new ACT ());
            proxy.createCustomer (customer1, new ACT ());
            proxy.createCustomer (customer2, new ACT ());
            proxy.createCustomer (customer3, new ACT ());
            proxy.createCustomer (customer4, new ACT ());
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        }
    }

    @After
    public void tearDown () {
        peer.processCommand ("quit");
    }

    @Test
    public void testLogin () {
        try {
            Assert.assertFalse (client.login ("non", "existant"));
            Assert.assertTrue (client.login ("dirk", "0626775"));
            Assert.assertFalse (client.login ("dirk", "wrong"));
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        }
    }
    
    @Test
    public void testOrder () {
        try {
            List<Product> products = (List<Product>) ((InvocationObject) proxy.listProducts (new ACT ())).getReturnParam();
            Assert.assertTrue (client.login ("dirk", "0626775"));
            client.addProduct (products.get (1));
            client.addProduct (products.get (2));
            client.buyOrder ();
            Customer customer = (Customer) ((InvocationObject) proxy.login ("dirk", "0626775", null)).getReturnParam();
            List<Order> orders = customer.getOrders ();
            Assert.assertEquals (orders.size (), 1);
            Assert.assertEquals (orders.get (0).getProducts ().size (), 2);
            Assert.assertEquals (orders.get (0).getProducts ().get(0).getName (), "Quietscheente");
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        }
    }
}

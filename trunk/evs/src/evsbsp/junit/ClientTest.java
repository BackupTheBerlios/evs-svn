package evsbsp.junit;

import evs.core.ACT;
import evs.exception.NotSupportedException;
import evsbsp.client.Client;
import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import evsbsp.entities.Customer;
import evsbsp.entities.Product;
import evsbsp.server.Ecommerce;

public class ClientTest {

    Client client;

    public ClientTest () {
    }

    @Before
    public void setUp () {
        client = new Client ();
    }

    @After
    public void tearDown () {
    }

    @Test
    public void testLogin () {
        try {
            Assert.assertNull (client.login ("non", "existant"));
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        }
    }
}

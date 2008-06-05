package evsbsp.junit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import evs.core.Common;
import evs.exception.NotSupportedException;
import evsbsp.client.Client;

public class ClientTest {

    Client client;

    public ClientTest () {
    }

    @Before
    public void setUp () {
        client = new Client ();
        Common.loadProperties();
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

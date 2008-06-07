package evsbsp.junit;

import dummy.IDummyOperations;
import dummy.client.DummyProxy;
import dummy.exception.DummyException;
import evs.core.InvocationObject;
import evs.core.InvocationStyle;
import evs.exception.NotSupportedException;
import evs.main.Peer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LifecycleTest {

    private static Peer peer;

    public LifecycleTest () {
    }

    @Before
    public void setUp () {
        peer = TestHelper.getPeer ();
    }

    @After
    public void tearDown () {
        peer.processCommand ("quit");
    }

    @Test
    public void testSTATIC () {
        peer.processCommand ("register-object=dummy.server.Dummy STATIC");
        try {
            IDummyOperations client1 = new DummyProxy (InvocationStyle.SYNC, null);
            client1.testCall (new Integer (1), null);
            client1.testCall (new Integer (3), null);
            Integer i = (Integer) ((InvocationObject) client1.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
            IDummyOperations client2 = new DummyProxy (InvocationStyle.SYNC, null);
            i = (Integer) ((InvocationObject) client2.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testSTATIC_PASSIVATION () {
        peer.processCommand ("register-object=dummy.server.Dummy STATIC_PASSIVATION");
        try {
            IDummyOperations client1 = new DummyProxy (InvocationStyle.SYNC, null);
            client1.testCall (new Integer (1), null);
            client1.testCall (new Integer (3), null);
            Integer i = (Integer) ((InvocationObject) client1.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
            try {
                Thread.sleep (250);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            IDummyOperations client2 = new DummyProxy (InvocationStyle.SYNC, null);
            i = (Integer) ((InvocationObject) client2.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testPER_REQUEST () {
        peer.processCommand ("register-object=dummy.server.Dummy PER_REQUEST");
        try {
            IDummyOperations client1 = new DummyProxy (InvocationStyle.SYNC, null);
            client1.testCall (new Integer (1), null);
            client1.testCall (new Integer (3), null);
            Integer i = (Integer) ((InvocationObject) client1.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 0);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testCLIENT () {
        peer.processCommand ("register-object=dummy.server.Dummy CLIENT");
        try {
            DummyProxy client1 = new DummyProxy (InvocationStyle.SYNC, null);
            client1.newInstance (null);
            client1.testCall (new Integer (1), null);
            client1.testCall (new Integer (3), null);
            Integer i = (Integer) ((InvocationObject) client1.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
            DummyProxy client2 = new DummyProxy (InvocationStyle.SYNC, null);
            client2.newInstance (null);
            i = (Integer) ((InvocationObject) client2.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 0);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testCLIENT_PASSIVATION () {
        peer.processCommand ("register-object=dummy.server.Dummy CLIENT_PASSIVATION");
        try {
            DummyProxy client1 = new DummyProxy (InvocationStyle.SYNC, null);
            client1.newInstance (null);
            client1.testCall (new Integer (1), null);
            client1.testCall (new Integer (3), null);
            Integer i = (Integer) ((InvocationObject) client1.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
            try {
                Thread.sleep (250);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            DummyProxy client2 = new DummyProxy (InvocationStyle.SYNC, null);
            client2.newInstance (null);
            i = (Integer) ((InvocationObject) client2.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 0);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testLAZY () {
        peer.processCommand ("register-object=dummy.server.Dummy LAZY");
        try {
            IDummyOperations client1 = new DummyProxy (InvocationStyle.SYNC, null);
            client1.testCall (new Integer (1), null);
            client1.testCall (new Integer (3), null);
            Integer i = (Integer) ((InvocationObject) client1.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 8);
            IDummyOperations client2 = new DummyProxy (InvocationStyle.SYNC, null);
            i = (Integer) ((InvocationObject) client2.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 8);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testPOOLING () {
        peer.processCommand ("register-object=dummy.server.Dummy POOLING");
        try {
            IDummyOperations client1 = new DummyProxy (InvocationStyle.SYNC, null);
            client1.testCall (new Integer (1), null);
            client1.testCall (new Integer (3), null);
            Integer i = (Integer) ((InvocationObject) client1.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
            IDummyOperations client2 = new DummyProxy (InvocationStyle.SYNC, null);
            i = (Integer) ((InvocationObject) client2.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    private class PassivationThread extends Thread {

        private long sleeptime;
        Integer i;

        PassivationThread (long sleeptime, Integer i) {
            this.sleeptime = sleeptime;
            this.i = i;
        }

        @Override
        public void run () {
            try {
                sleep (sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            try {
                IDummyOperations client2 = new DummyProxy (InvocationStyle.SYNC, null);
                i = (Integer) ((InvocationObject) client2.getCounter (null)).getReturnParam ();
                Assert.assertEquals (i, 4);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
    }
}

package evsbsp.junit;

import dummy.IDummyOperations;
import dummy.client.DummyProxy;
import dummy.exception.DummyException;
import evs.core.ACT;
import evs.core.InvocationObject;
import evs.core.InvocationStyle;
import evs.core.PollObject;
import evs.exception.NotSupportedException;
import evs.interfaces.IACT;
import evs.interfaces.ICallback;
import evs.interfaces.IPollObject;
import evs.main.Peer;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InvocationTest {

    private static Peer peer;

    public InvocationTest () {
    }

    @Before
    public void setUp () {
//        String[] args = {};
//        if (peer == null) {
//        peer = new Peer (args);
//        peer.run ();
        peer = TestHelper.getPeer ();
        peer.processCommand ("register-object=dummy.server.Dummy");
//        peer.processCommand ("port=31337");
//        peer.processCommand ("listen");
//        }
    }

    @After
    public void tearDown () {
        peer.processCommand ("quit");
    }

    @Test
    public void testSYNC () {
        try {
            IDummyOperations proxy = new DummyProxy (InvocationStyle.SYNC, null);
            proxy.testCall (new Integer (1), null);
            Integer i = (Integer) ((InvocationObject) proxy.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 1);
            proxy.testCall (new Integer (3), null);
            i = (Integer) ((InvocationObject) proxy.getCounter (null)).getReturnParam ();
            Assert.assertEquals (i, 4);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testFIRE_FORGET () {
        try {
            DummyProxy proxy = new DummyProxy (InvocationStyle.FIRE_FORGET, null);
            proxy.testCall (new Integer (1), null);
            proxy.setRequestType (InvocationStyle.SYNC);
            Integer i = (Integer) ((InvocationObject) proxy.getCounter (null)).getReturnParam ();
//            Assert.assertEquals (i, 1);
            proxy.setRequestType (InvocationStyle.FIRE_FORGET);
            proxy.testCall (new Integer (3), null);
            proxy.setRequestType (InvocationStyle.SYNC);
            i = (Integer) ((InvocationObject) proxy.getCounter (null)).getReturnParam ();
//            Assert.assertEquals (i, 4);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testPOLL_OBJECT () {
        try {
            DummyProxy proxy = new DummyProxy (InvocationStyle.POLL_OBJECT, null);
            PollThread pollThread = new PollThread ((PollObject) proxy.getCounter (null), new Integer (0));
            pollThread.start ();
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    @Test
    public void testRESULT_CALLBACK () {
        try {
            ResultCallback callback = new ResultCallback ();
            DummyProxy proxy = new DummyProxy (InvocationStyle.RESULT_CALLBACK, callback);
            IACT act = new ACT ();
            callback.addACT (act, new Integer (0));
            proxy.getCounter (act);
        } catch (NotSupportedException e) {
            e.printStackTrace ();
        } catch (DummyException e) {
            e.printStackTrace ();
        }
    }

    private class PollThread extends Thread {

        private IPollObject pollObject;
        Integer i;

        PollThread (IPollObject pollObject, Integer i) {
            this.pollObject = pollObject;
            this.i = i;
        }

        @Override
        public void run () {
            while (!pollObject.isResultAvailable ()) {
                try {
                    sleep (10);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
            try {
                Assert.assertEquals ((Integer) pollObject.getResult (), i);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
    }
    
    public class ResultCallback implements ICallback {
        
        private List<ACTAssertContainer> acts;
        
        ResultCallback () {
            acts = new ArrayList<ACTAssertContainer> ();
        }
        
        public void addACT (IACT act, Integer i) {
            acts.add (new ACTAssertContainer (act, i));
        }

        public synchronized void resultReturned (IACT act, Object result) {
            for (ACTAssertContainer tmp : acts) {
                if (tmp.getACT ().equals (act) && tmp.getI () != null) {
                    Assert.assertEquals ((Integer) result, tmp.getI ());
                    acts.remove(tmp);
                    break;
                }
            }
        }
        
        private class ACTAssertContainer {
            
            private Integer i;
            private IACT act;
            
            ACTAssertContainer (IACT act, Integer i) {
                this.act = act;
                this.i = i;
            }
            
            public IACT getACT () {
                return this.act;
            }
            
            public Integer getI () {
                return this.i;
            }
        }
    }
}

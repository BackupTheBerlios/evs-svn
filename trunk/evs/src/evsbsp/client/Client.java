package evsbsp.client;

import evs.core.ACT;
import evs.core.Common;
import evs.core.LifecycleStrategy;
import evs.core.ObjectReference;
import evs.interfaces.IACT;
import evs.interfaces.ICallback;
import evs.interfaces.IObjectReference;
import evsbsp.server.Dummy;
import evsbsp.server.DummyInvoker;


public class Client implements ICallback{


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client cli = new Client();
		cli.run();

	}

	private void run() {
		// TODO Auto-generated method stub
		DummyProxy dummy = new DummyProxy();
		
		// TODO TEMPORARY call lifecycle manager here:
		try {
			Common.loadProperties();
			IObjectReference ref = new ObjectReference(Dummy.class.getName(), DummyInvoker.class.getName());
			Common.getObjectManager().register(ref, LifecycleStrategy.CLIENT);
			Common.getInvocationDispatcher().registerInvoker(DummyInvoker.class.getName(), new DummyInvoker());
		} catch (Exception ex){
			System.out.println("[x] Exception caught: " + ex.getMessage());
		}
		
		try {
			
			IACT act = new ACT();
			
			dummy.newInstance(this, act);
			Integer value = dummy.getCounter(this, act);
			System.out.println("[*] Initial value=" + value);
			dummy.testCall(new Integer(1337));
			value = dummy.getCounter(this, act);
			System.out.println("[*] Value after incrementing=" + value);
			dummy.testCall(new Integer(1));
			value = dummy.getCounter(this, act);
			System.out.println("[*] Value after incrementing=" + value);
			dummy.testCall(new Integer(1));
			value = dummy.getCounter(this, act);
			System.out.println("[*] Value after incrementing=" + value);
			Thread.sleep(1000);
			value = dummy.getCounter(this, act);
			System.out.println("[*] Value after incrementing=" + value);
			dummy.keepAlive();
			Thread.sleep(5);
			value = dummy.getCounter(this, act);
			System.out.println("[*] Value after incrementing=" + value);
			Thread.sleep(100);
			value = dummy.getCounter(this, act);
			System.out.println("[*] Value after incrementing=" + value);
			//dummy.destroy();
			Thread.sleep(10000);
			
			value = dummy.getCounter(this, act);
			System.out.println("[*] Value after incrementing=" + value);
		} catch (Exception ex){
			System.out.println("[x] Exception caught: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	public void resultReturned(IACT act, Object result) {
		// TODO Auto-generated method stub
		
	}

}

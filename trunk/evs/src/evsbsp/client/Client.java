package evsbsp.client;

import evs.core.Common;
import evs.core.LifecycleStrategy;
import evs.core.ObjectReference;
import evs.interfaces.IObjectReference;
import evsbsp.IDummyOperations;
import evsbsp.server.Dummy;
import evsbsp.server.DummyInvoker;

public class Client {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DummyProxy dummy = new DummyProxy();
		
		//TEMPORARY call lifecycle manager here:
		try {
			Common.loadProperties();
			IObjectReference ref = new ObjectReference(Dummy.class.getName(), DummyInvoker.class.getName());
			Common.getObjectManager().register(ref, LifecycleStrategy.CLIENT);
			Common.getInvocationDispatcher().registerInvoker(DummyInvoker.class.getName(), new DummyInvoker());
		} catch (Exception ex){
			System.out.println("[x] Exception caught: " + ex.getMessage());
		}
		
		try {
			dummy.newInstance();
			Integer value = dummy.getCounter();
			System.out.println("[*] Initial value=" + value);
			dummy.testCall(new Integer(1337));
			value = dummy.getCounter();
			System.out.println("[*] Value after incrementing=" + value);
			dummy.testCall(new Integer(1));
			value = dummy.getCounter();
			System.out.println("[*] Value after incrementing=" + value);
			dummy.testCall(new Integer(1));
			value = dummy.getCounter();
			System.out.println("[*] Value after incrementing=" + value);
			Thread.sleep(1000);
			value = dummy.getCounter();
			System.out.println("[*] Value after incrementing=" + value);
			dummy.keepAlive();
			Thread.sleep(5);
			value = dummy.getCounter();
			System.out.println("[*] Value after incrementing=" + value);
			Thread.sleep(100);
			value = dummy.getCounter();
			System.out.println("[*] Value after incrementing=" + value);
			//dummy.destroy();
			Thread.sleep(10000);
			
			value = dummy.getCounter();
			System.out.println("[*] Value after incrementing=" + value);
		} catch (Exception ex){
			System.out.println("[x] Exception caught: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}

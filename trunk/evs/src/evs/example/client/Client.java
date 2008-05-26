package evs.example.client;

import evs.comm.impl.Common;
import evs.comm.impl.LifecycleStrategy;
import evs.comm.impl.ObjectReference;
import evs.comm.interfaces.IObjectReference;
import evs.example.IDummyOperations;
import evs.example.server.Dummy;
import evs.example.server.DummyInvoker;

public class Client {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IDummyOperations dummy = new DummyProxy();
		
		//TEMPORARY call lifecycle manager here:
		try {
			Common.loadProperties();
			IObjectReference ref = new ObjectReference(Dummy.class.getName(), DummyInvoker.class.getName());
			Common.getObjectManager().register(ref, LifecycleStrategy.STATIC);
			Common.getInvocationDispatcher().registerInvoker(DummyInvoker.class.getName(), new DummyInvoker());
		} catch (Exception ex){
			System.out.println("[x] Exception caught: " + ex.getMessage());
		}
		
		try {
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
		} catch (Exception ex){
			System.out.println("[x] Exception caught: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}

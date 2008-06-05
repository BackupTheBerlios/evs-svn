package dummy.client;

import dummy.server.Dummy;
import dummy.server.DummyInvoker;
import dummy.client.DummyProxy;
import evs.core.ACT;
import evs.core.Common;
import evs.core.InvocationStyle;
import evs.core.LifecycleStrategy;
import evs.core.ObjectReference;
import evs.interfaces.IACT;
import evs.interfaces.ICallback;
import evs.interfaces.IObjectReference;
import evs.interfaces.IPollObject;


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
		DummyProxy dummy = new DummyProxy(this);
		
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
			
			// loop for isResultAvailable()
//			IPollObject poll = dummy.testCallPoll(1);
			
//			dummy.newInstance(act);
//			Integer value = dummy.getCounter(act);
//			System.out.println("[*] Initial value=" + value);
//			dummy.testCall(new Integer(1337), null);
//			value = dummy.getCounter(act);
//			System.out.println("[*] Value after incrementing=" + value);
//			dummy.testCall(new Integer(1), null);
//			value = dummy.getCounter(act);
//			System.out.println("[*] Value after incrementing=" + value);
//			dummy.testCall(new Integer(1), null);
//			value = dummy.getCounter(act);
//			System.out.println("[*] Value after incrementing=" + value);
//			Thread.sleep(1000);
//			value = dummy.getCounter(act);
//			System.out.println("[*] Value after incrementing=" + value);
//			dummy.keepAlive();
//			Thread.sleep(5);
//			value = dummy.getCounter(act);
//			System.out.println("[*] Value after incrementing=" + value);
//			Thread.sleep(100);
//			value = dummy.getCounter(act);
//			System.out.println("[*] Value after incrementing=" + value);
//			//dummy.destroy();
//			Thread.sleep(10000);
			
			//dummy.newInstance(new ACT());
			dummy.setRequestType(InvocationStyle.POLL_OBJECT);
			
			IPollObject poll = (IPollObject)dummy.getCounter(null);
			while(!poll.isResultAvailable())
				Thread.sleep(1000);
			System.out.println("Got result: " + poll.getResult());
			
			poll = (IPollObject)dummy.testCall(2, null);
			while(!poll.isResultAvailable())
				Thread.sleep(1000);
			System.out.println("Got result: " + poll.getResult() + ", hopefully the counter was increased.");
			
			poll = (IPollObject)dummy.getCounter(null);
			while(!poll.isResultAvailable())
				Thread.sleep(1000);
			System.out.println("Got result: " + poll.getResult());
			
//			// fire and forget
//			dummy.setRequestType(InvocationStyle.FIRE_FORGET);
//			dummy.testCall(5, null);
//			dummy.getCounter(new ACT());
//			
//			// callback
//			dummy.setRequestType(InvocationStyle.RESULT_CALLBACK);
//			dummy.testCall(3, new ACT());
//			dummy.getCounter(new ACT());
			
			
		} catch (Exception ex){
			System.out.println("[x] Exception caught: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	public void resultReturned(IACT act, Object result) {
		// TODO Auto-generated method stub
		Integer i = (Integer)result;
		System.out.println("[*] Value after incrementing=" + i + ", ACT = " + act.getTimestamp());
	}

}

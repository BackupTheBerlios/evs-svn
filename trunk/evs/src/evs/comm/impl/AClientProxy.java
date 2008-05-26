package evs.comm.impl;

import java.util.ArrayList;

import evs.comm.exception.NotSupportedException;
import evs.comm.exception.RemotingException;
import evs.comm.interfaces.IAOR;
import evs.comm.interfaces.IClientProxy;
import evs.comm.interfaces.IInvocationObject;
import evs.comm.interfaces.IRequestor;
import evs.example.exception.DummyException;

public abstract class AClientProxy implements IClientProxy{

	protected IRequestor requestor;
	protected IAOR aor;
	
	public AClientProxy(){
		this.requestor = new Requestor();
	}

	public IAOR getAOR() {
		return aor;
	}

	public void setAOR(IAOR aor) {
		this.aor = aor;
	}
	
	public void newInstance() throws NotSupportedException{
		ArrayList<Object> arguments = new ArrayList<Object>();
		try{
			IInvocationObject object = new InvocationObject(getAOR(), "newInstance", arguments, "String");
			String objectId = (String) requestor.invoke(object, false);
			this.aor.getReference().setInstanceId(objectId);
		} catch(RemotingException ex){
			 if(ex instanceof NotSupportedException) throw (NotSupportedException) ex;
			 System.out.println("[x] ERROR: " + ex.getClass().getName() + " :" + ex.getMessage());
			 ex.printStackTrace();
		}
	}
	
	public void keepAlive() throws NotSupportedException {
		ArrayList<Object> arguments = new ArrayList<Object>();
		try{
			IInvocationObject object = new InvocationObject(getAOR(), "keepAlive", arguments, "void");
			requestor.invoke(object, true);
		} catch(RemotingException ex){
			 if(ex instanceof NotSupportedException) throw (NotSupportedException) ex;
			 System.out.println("[x] ERROR: " + ex.getClass().getName() + " :" + ex.getMessage());
			 ex.printStackTrace();
		}
	}
	
	public void destroy() throws NotSupportedException {
		ArrayList<Object> arguments = new ArrayList<Object>();
		try{
			IInvocationObject object = new InvocationObject(getAOR(), "destroy", arguments, "void");
			requestor.invoke(object, true);
		} catch(RemotingException ex){
			 if(ex instanceof NotSupportedException) throw (NotSupportedException) ex;
			 System.out.println("[x] ERROR: " + ex.getClass().getName() + " :" + ex.getMessage());
			 ex.printStackTrace();
		}
	}
	
	
}

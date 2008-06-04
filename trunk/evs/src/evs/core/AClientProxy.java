package evs.core;

import java.util.ArrayList;

import evs.exception.NotSupportedException;
import evs.exception.RemotingException;
import evs.interfaces.IACT;
import evs.interfaces.IAOR;
import evs.interfaces.ICallback;
import evs.interfaces.IClientProxy;
import evs.interfaces.IInvocationObject;
import evs.interfaces.IRequestor;

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
	
	public void newInstance(ICallback callback, IACT act) throws NotSupportedException{
		ArrayList<Object> arguments = new ArrayList<Object>();
		try{
			IInvocationObject object = new InvocationObject(getAOR(), "newInstance", arguments, "String");
			String objectId = (String) requestor.invoke(object, false, callback, act);
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

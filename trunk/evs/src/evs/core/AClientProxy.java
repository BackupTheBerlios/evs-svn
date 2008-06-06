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
	protected InvocationStyle requestType;
	protected ICallback callback;
	
	public AClientProxy(){
		this.requestor = new Requestor();
		requestType = InvocationStyle.FIRE_FORGET;
	}
	
	public AClientProxy(ICallback callback){
		this.requestor = new Requestor();
		this.callback = callback;
		requestType = InvocationStyle.RESULT_CALLBACK;
	}

	public AClientProxy(InvocationStyle requestType, ICallback callback){
		this.requestor = new Requestor();
		this.requestType = requestType;
	}
	
	public IAOR getAOR() {
		return aor;
	}

	public void setAOR(IAOR aor) {
		this.aor = aor;
	}
	
	public void newInstance(IACT act) throws NotSupportedException{
		ArrayList<Object> arguments = new ArrayList<Object>();
    arguments.add(act);
		try{
			IInvocationObject object = new InvocationObject(getAOR(), "newInstance", arguments, "String");
			String objectId = (String) requestor.invoke(object, false, callback, act, InvocationStyle.SYNC);
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
			requestor.invoke(object, true, null, null, InvocationStyle.FIRE_FORGET);
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
			requestor.invoke(object, true, null, null, InvocationStyle.FIRE_FORGET);
		} catch(RemotingException ex){
			 if(ex instanceof NotSupportedException) throw (NotSupportedException) ex;
			 System.out.println("[x] ERROR: " + ex.getClass().getName() + " :" + ex.getMessage());
			 ex.printStackTrace();
		}
	}

	public InvocationStyle getRequestType() {
		return requestType;
	}

	public void setRequestType(InvocationStyle requestType) {
		this.requestType = requestType;
	}

	public ICallback getCallback() {
		return callback;
	}

	public void setCallback(ICallback callback) {
		this.callback = callback;
	}
}

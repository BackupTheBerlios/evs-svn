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

/**
 * AClientProxy
 * abstract implementation of a client proxy
 * 
 * @author Dirk Wallerstorfer
 * 
 */
public abstract class AClientProxy implements IClientProxy{

	protected IRequestor requestor;
	protected IAOR aor;
	protected InvocationStyle requestType;
	protected ICallback callback;
	
	/**
	 * emtpy constructor
	 */
	public AClientProxy(){
		this.requestor = new Requestor();
		requestType = InvocationStyle.FIRE_FORGET;
	}
	
	/**
	 * constructor with ICallback
	 * @param callback the callback to be used when InvocationStyle is RESULT_CALLBACK
	 */
	public AClientProxy(ICallback callback){
		this.requestor = new Requestor();
		this.callback = callback;
		requestType = InvocationStyle.RESULT_CALLBACK;
	}

	/**
	 * constructor with InvocationStyle and ICallback
	 * @param requestType InvocationStyle to be used for communication
	 * @param callback the callback to be used when InvocationStyle is RESULT_CALLBACK
	 */
	public AClientProxy(InvocationStyle requestType, ICallback callback){
		this.requestor = new Requestor();
		this.requestType = requestType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IClientProxy#getAOR()
	 */
	public IAOR getAOR() {
		return aor;
	}

	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IClientProxy#setAOR(evs.interfaces.IAOR)
	 */
	public void setAOR(IAOR aor) {
		this.aor = aor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IClientProxy#newInstance(evs.interfaces.IACT)
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IClientProxy#keepAlive()
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see evs.interfaces.IClientProxy#destroy()
	 */
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

	/**
	 * get the InvocationStyle
	 * @return the user InvocationStyle
	 */
	public InvocationStyle getRequestType() {
		return requestType;
	}

	/**
	 * set the InvocationStyle
	 * @param requestType InvocationStyle to be used
	 */
	public void setRequestType(InvocationStyle requestType) {
		this.requestType = requestType;
	}

	/**
	 * get the ICallback set
	 * @return returns ICallback set
	 */
	public ICallback getCallback() {
		return callback;
	}

	/**
	 * sets the ICallback
	 * @param callback ICallback to be set
	 */
	public void setCallback(ICallback callback) {
		this.callback = callback;
	}
}

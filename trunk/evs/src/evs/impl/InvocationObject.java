package evs.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import evs.exception.RemotingException;
import evs.interfaces.IAOR;
import evs.interfaces.IInvocationContext;
import evs.interfaces.IInvocationObject;

public class InvocationObject implements IInvocationObject {

	public static final long serialVersionUID = 1337;
	
	private IAOR objectReference = null;
	private String operationName = null;
	private ArrayList<Object> arguments = new ArrayList<Object>();
	private Object returnParam = null;
	private InvocationStyle requestType = InvocationStyle.SYNC;
	private RemotingException remoteException = new RemotingException();
	private ArrayList<IInvocationContext> contexts = new ArrayList<IInvocationContext>();
	
	public InvocationObject() {}
	
	public InvocationObject(IAOR objectReference, String operationName) {
		this.objectReference = objectReference;
		this.operationName = operationName;
	}
	
	public InvocationObject(IAOR objectReference, String operationName,
			ArrayList<Object> arguments, Object returnParam) {
		this.objectReference = objectReference;
		this.operationName = operationName;
		this.arguments = arguments;
		this.returnParam = returnParam;
	}

	public InvocationObject(IAOR objectReference, String operationName,
			ArrayList<Object> arguments, Object returnParam,
			InvocationStyle requestType, RemotingException remoteException) {
		this.objectReference = objectReference;
		this.operationName = operationName;
		this.arguments = arguments;
		this.returnParam = returnParam;
		this.requestType = requestType;
		this.remoteException = remoteException;
	}

	public IAOR getObjectReference() {
		return objectReference;
	}

	public void setObjectReference(IAOR objectReference) {
		this.objectReference = objectReference;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public List<Object> getArguments() {
		return arguments;
	}

	public void setArguments(ArrayList<Object> arguments) {
		this.arguments = arguments;
	}

	public void addArgument(Object argument){
		this.arguments.add(argument);
	}
	
	public Object getReturnParam() {
		return returnParam;
	}

	public void setReturnParam(Object returnParam) {
		this.returnParam = returnParam;
	}

	public InvocationStyle getRequestType() {
		return requestType;
	}

	public void setRequestType(InvocationStyle requestType) {
		this.requestType = requestType;
	}

	public RemotingException getRemoteException() {
		return remoteException;
	}

	public void setRemoteException(RemotingException remoteException) {
		this.remoteException = remoteException;
	}
	

	public void addInvocationContext(IInvocationContext context){
		this.contexts.add(context);
	}
	
	public void setInvocationContext(List<IInvocationContext> contexts){
		this.contexts.addAll(contexts);
	}
	
	public List<IInvocationContext> getInvocationContext(){
		return this.contexts;
	}
	
	public synchronized void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(objectReference);
		out.writeUTF(operationName);
		out.writeInt(arguments.size());
		for (int i=0; i<arguments.size(); i++)  
			out.writeObject(arguments.get(i)); 
		out.writeObject(returnParam);
		out.writeUTF(requestType.name());
		out.writeObject(remoteException);
		out.writeInt(contexts.size());
		for(int i=0; i<contexts.size(); i++)
			out.writeObject(contexts.get(i)); 
	}
	
	public synchronized void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		this.objectReference = (IAOR) in.readObject();
		this.operationName = in.readUTF();
		int len = in.readInt();
		this.arguments = new ArrayList<Object>();
		for(int i=0; i<len; i++)
			this.arguments.add(in.readObject());
		this.returnParam = in.readObject();
		this.requestType = java.lang.Enum.valueOf(InvocationStyle.class, in.readUTF());
		this.remoteException =  (RemotingException) (in.readObject());
		len = in.readInt();
		for(int i=0; i<len; i++)
			this.contexts.add((IInvocationContext) (in.readObject()));
	}
	
}

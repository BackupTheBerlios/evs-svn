package evs.comm;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import evs.exception.RemotingException;

public class InvocationObject implements Externalizable {

	public static final long serialVersionUID = 1337;
	
	private IAOR objectReference;
	private String operationName;
	private ArrayList<Object> arguments;
	private Object returnParam;
	private InvocationStyle requestType = InvocationStyle.SYNC;
	private RemotingException remoteException;
	
	public InvocationObject() {}
	
	public InvocationObject(IAOR objectReference, String operationName) {
		this.objectReference = objectReference;
		this.operationName = operationName;
		this.arguments = new ArrayList<Object>();
		this.remoteException = new RemotingException();
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
	
	public String toString(){
		return objectReference + "|" + operationName + "|" + arguments;
	}
	
	public synchronized void writeExternal(ObjectOutput out) throws IOException {
		//TODO: serialize AOR?
		out.writeUTF(operationName);
		out.writeInt(arguments.size());
		for (int i=0; i<arguments.size(); i++)  
			out.writeObject(arguments.get(i)); 
		out.writeObject(returnParam);
		out.writeUTF(requestType.name());
		out.writeUTF(remoteException.getMessage());
		
	}
	
	public synchronized void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		//TODO: deserialize AOR?
		this.operationName = in.readUTF();
		int len = in.readInt();
		this.arguments = new ArrayList<Object>();
		for(int i=0; i<len; i++){
			Object obj = in.readObject();
			arguments.add(obj);
		}
		this.returnParam = in.readObject();
		this.requestType = java.lang.Enum.valueOf(InvocationStyle.class, in.readUTF());
		this.remoteException = new RemotingException(in.readUTF());
	}
	
}

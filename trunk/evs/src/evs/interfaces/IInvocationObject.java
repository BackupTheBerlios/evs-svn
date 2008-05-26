package evs.interfaces;

import java.io.Externalizable;
import java.util.ArrayList;
import java.util.List;

import evs.core.InvocationStyle;
import evs.exception.RemotingException;

public interface IInvocationObject extends Externalizable {
	
	IAOR getObjectReference();
	void setObjectReference(IAOR objectReference);

	String getOperationName();

	void setOperationName(String operationName);

	List<Object> getArguments();
	void setArguments(ArrayList<Object> arguments);
	void addArgument(Object argument);
	
	Object getReturnParam();
	void setReturnParam(Object returnParam);

	InvocationStyle getRequestType();
	void setRequestType(InvocationStyle requestType);
	
	RemotingException getRemoteException();
	void setRemoteException(RemotingException remoteException);
	
	void addInvocationContext(IInvocationContext context);
	void setInvocationContext(List<IInvocationContext> contexts);
	List<IInvocationContext> getInvocationContext();
}

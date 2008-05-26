package evs.interfaces;

import java.io.Externalizable;

public interface IObjectReference extends Externalizable {
	
	String getObjectId();
	void setObjectId(String objectId);
	
	String getInvokerId();
	void setInvokerId(String invokerId);
	
	String getInstanceId();
	void setInstanceId(String instanceId);
	
	String getClientDependent();
	String getClientIndependent();
	
}

package evs.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import evs.interfaces.IObjectReference;

public class ObjectReference implements IObjectReference {

	String objectId;
	String invokerId;
	String instanceId;
	
	public ObjectReference(){}
	
	public ObjectReference(String objectId, String invokerId){
		this.objectId = objectId;
		this.invokerId = invokerId;
		this.instanceId = "";
	}
	
	public ObjectReference(String objectId, String invokerId, String instanceId){
		this(objectId, invokerId);
		this.instanceId = instanceId;
	}
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getInvokerId() {
		return invokerId;
	}
	public void setInvokerId(String invokerId) {
		this.invokerId = invokerId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	public String getClientDependent(){
		return getClientIndependent() + "@" + getInstanceId();
	}
	
	public String getClientIndependent(){
		return getInvokerId() + "@" + getObjectId();
	}
	
	public synchronized void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.objectId);
		out.writeUTF(this.invokerId);
		out.writeUTF(this.instanceId);
	}
	
	public synchronized void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		this.objectId = in.readUTF();
		this.invokerId = in.readUTF();
		this.instanceId = in.readUTF();
	}
}

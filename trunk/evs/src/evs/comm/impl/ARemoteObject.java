package evs.comm.impl;

import java.io.Serializable;

import evs.comm.interfaces.IRemoteObject;

public abstract class ARemoteObject implements IRemoteObject {

	protected String objectId;
	protected Long expiration;

	public String getId(){
		return this.objectId;
	}
	
	public void setId(String id){
		this.objectId = id;
	}
	
	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}
	
}

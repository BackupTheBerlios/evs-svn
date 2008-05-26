package evs.comm.impl;

import evs.comm.interfaces.IObjectState;
import evs.comm.interfaces.IRemoteObject;

public class ObjectState implements IObjectState {

	private boolean persisted;
	private IRemoteObject object;
	private boolean locked;
	
	
	public ObjectState(IRemoteObject object) {
		this.object = object;
		this.persisted = false;
		this.locked = false;
	}


	public ObjectState(boolean persisted, IRemoteObject object) {
		this.persisted = persisted;
		this.object = object;
		this.locked = false;
	}


	public boolean isPersisted() {
		return persisted;
	}


	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}


	public IRemoteObject getObject() {
		return object;
	}


	public void setObject(IRemoteObject object) {
		this.object = object;
	}


	public boolean isLocked() {
		return locked;
	}


	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}

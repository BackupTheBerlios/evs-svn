package evs.core;

import evs.interfaces.IObjectState;
import evs.interfaces.IRemoteObject;

public class ObjectState implements IObjectState {

	private boolean persisted;
	private IRemoteObject object;
	private boolean locked;
	
	
	public ObjectState(IRemoteObject object) {
		this.object = object;
		this.persisted = false;
		this.locked = false;
	}


	public ObjectState(IRemoteObject object, boolean persisted) {
		this.object = object;
		this.persisted = persisted;
		this.locked = false;
	}

	public IRemoteObject getObject() {
		return object;
	}


	public void setObject(IRemoteObject object) {
		this.object = object;
	}

	public boolean isPersisted() {
		return persisted;
	}


	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}


	public boolean isLocked() {
		return locked;
	}


	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}

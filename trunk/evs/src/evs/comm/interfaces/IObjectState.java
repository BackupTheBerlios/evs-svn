package evs.comm.interfaces;

public interface IObjectState {

	boolean isPersisted();
	void setPersisted(boolean persisted);
	
	IRemoteObject getObject();
	void setObject(IRemoteObject object);
	
	boolean isLocked();
	void setLocked(boolean locked);
	
}

package evs.interfaces;

public interface IObjectState {

	IRemoteObject getObject();
	void setObject(IRemoteObject object);
	
	boolean isPersisted();
	void setPersisted(boolean persisted);
	
	boolean isLocked();
	void setLocked(boolean locked);
	
}

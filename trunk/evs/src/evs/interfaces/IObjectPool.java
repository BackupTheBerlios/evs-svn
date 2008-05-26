package evs.interfaces;

import evs.exception.IllegalObjectException;

public interface IObjectPool {
	
	void createPool(Integer poolSize) throws IllegalObjectException;
	
	IRemoteObject checkOut() throws IllegalObjectException;
	
	void checkIn(IRemoteObject object);
	
	public void cleanUp();
}

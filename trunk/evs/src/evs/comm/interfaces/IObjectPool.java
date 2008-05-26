package evs.comm.interfaces;

import evs.comm.exception.IllegalObjectException;

public interface IObjectPool {
	
	void createPool(Integer poolSize) throws IllegalObjectException;
	
	IRemoteObject checkOut() throws IllegalObjectException;
	
	void checkIn(IRemoteObject object);
	
	public void cleanUp();
}

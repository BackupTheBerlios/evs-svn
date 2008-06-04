package evs.interfaces;

import evs.core.ACT;

public interface ICallback {
	
//	void setResult(Object object);
//	void setError(RemotingException exception);
//	
//	Object getResult() throws RemotingException;
//	boolean finished();

	void resultReturned(ACT act, Object result);
	
}

package evs.interfaces;

import evs.exception.RemotingException;

public interface ICallback {
	
	void setResult(Object object);
	void setError(RemotingException exception);
	
	Object getResult() throws RemotingException;
	boolean finished();

}

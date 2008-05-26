package evs.comm.interfaces;

import evs.comm.exception.RemotingException;

public interface ICallback {
	
	void setResult(Object object);
	void setError(RemotingException exception);
	
	Object getResult() throws RemotingException;
	boolean finished();

}

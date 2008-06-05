package evs.interfaces;


public interface ICallback {
	
//	void setResult(Object object);
//	void setError(RemotingException exception);
//	
//	Object getResult() throws RemotingException;
//	boolean finished();

	void resultReturned(IACT act, Object result);
	
}

package evs.core;

import evs.exception.RemotingException;
import evs.interfaces.ICallback;

public class Callback implements ICallback {

	private boolean finished;
	private Object result;
	private RemotingException error;
	
	public Callback(){
		this.finished = false;
		this.result = null;
		this.error = null;
	}
	
	public boolean finished(){
		return this.finished;
	}
	
	public Object getResult() throws RemotingException{
		if(this.error != null) throw error;
		return this.result;
	}
	
	public void setResult(Object object){
		this.result = object;
		this.finished = true;
	}
	
	public void setError(RemotingException exception){
		this.error = exception;
		this.finished = true;
	}
}

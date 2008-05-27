package evs.core;

import java.util.HashSet;

import evs.exception.IllegalObjectException;
import evs.interfaces.IObjectPool;
import evs.interfaces.IRemoteObject;

/* see http://www.javaworld.com/jw-06-1998/jw-06-object-pool.html */
/* see http://www.javaworld.com/jw-08-1998/jw-08-object-pool.html */
public class ObjectPool implements IObjectPool {
	
	private HashSet<IRemoteObject> unlocked;
	private HashSet<IRemoteObject> locked;
	
	private Integer poolSize;
	private Long expirationTime;
	private String type;
	
	private CleanUpThread cleaner;
	
	public ObjectPool(String type, Long expirationTime){
		this.unlocked = new HashSet<IRemoteObject>();
		this.locked = new HashSet<IRemoteObject>();
		this.expirationTime = expirationTime;
		this.type = type;
		this.poolSize = 0;
		this.cleaner = new CleanUpThread(this, expirationTime);
		cleaner.start();
	}
	
	public void createPool(Integer poolSize) throws IllegalObjectException {
		this.poolSize = poolSize;
		for(int i=0; i<poolSize.intValue(); i++){
			IRemoteObject obj = create();
			if(obj == null) throw new IllegalObjectException();
			obj.setExpiration(System.currentTimeMillis());
			this.unlocked.add(obj);
		}
	}
	

	
	public synchronized IRemoteObject checkOut() throws IllegalObjectException{
		IRemoteObject object;
		long now = System.currentTimeMillis();
		if(unlocked.isEmpty()){
			// no objects available, create a new one
			object = create();  	
		} else {
			object = unlocked.iterator().next();
			unlocked.remove(object);
		}
		object.setExpiration(new Long(now));
		locked.add(object);
	    return object;
	}
	
	public synchronized void checkIn(IRemoteObject object){
		locked.remove(object);
		object.setExpiration(System.currentTimeMillis());
		unlocked.add(object);
	}
	
	
	public void cleanUp(){
		if(unlocked.size() <= this.poolSize) return;
		long now = System.currentTimeMillis();
		HashSet<IRemoteObject> todelete = new HashSet<IRemoteObject>(); 
	    for(IRemoteObject object: unlocked){    
	    	if((now - ((Long) object.getExpiration()).longValue()) > expirationTime){
	    		todelete.add(object);
	        }
	    }
	    for(IRemoteObject obj: todelete){
	    	unlocked.remove(obj);
	    	obj = null;
	    }
	    System.gc();
	}
	
	private IRemoteObject create() throws IllegalObjectException{
		try {
			return (IRemoteObject) Class.forName(type).newInstance();
		} catch(Exception ex){
			throw new IllegalObjectException();
		}
	}
	
	//Thread for periodically cleaning up expired objects
	class CleanUpThread extends Thread {
		private ObjectPool pool;
		private long sleepTime;
		
		public CleanUpThread(ObjectPool pool, long sleepTime){
			this.pool = pool;
			this.sleepTime = sleepTime;
		}
		
		public void run(){
			while(true){
				try{
					sleep(sleepTime);
				} catch(InterruptedException ex){
					//ignore
				}
				pool.cleanUp();
			}
		}
		
	}
	
}
